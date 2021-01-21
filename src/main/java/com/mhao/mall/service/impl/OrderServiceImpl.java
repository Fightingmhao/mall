package com.mhao.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhao.mall.dao.OrderItemMapper;
import com.mhao.mall.dao.OrderMapper;
import com.mhao.mall.dao.ProductMapper;
import com.mhao.mall.dao.ShippingMapper;
import com.mhao.mall.enums.OrderStatusEnum;
import com.mhao.mall.enums.PaymentTypeEnum;
import com.mhao.mall.enums.ProductStatusEnum;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.pojo.*;
import com.mhao.mall.service.ICartService;
import com.mhao.mall.service.IOrderService;
import com.mhao.mall.vo.OrderItemVo;
import com.mhao.mall.vo.OrderVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单相关：1、下单 2、查询订单列表 3、查看订单详情
 * Created by FightingHao on 2020/4/30
 */

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收货地址shipping校验（总之要查出来的）
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }

        //获取购物车(仅取出选中的商品)，校验（是否有商品、库存）
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer, Product> map  = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            //根据productId查数据库
            Product product = map.get(cart.getProductId());
            //是否有商品
            if (product == null) {
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,
                        "商品不存在. productId = " + cart.getProductId());
            }
            //商品上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus())) {
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE,
                        "商品不是在售状态. " + product.getName());
            }

            //库存是否充足
            if (product.getStock() < cart.getQuantity()) {
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,
                        "库存不够. " + product.getName());
            }

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }

        //计算总价，只计算选中的商品
        //生成订单，入库：order和order_item，事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);

        int rowForOrder = orderMapper.insertSelective(order);
        if (rowForOrder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        //更新购物车（选中的商品下了订单，那么购物车中就没此商品了）
        //Redis的事务(打包命令)，不能回滚
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);

        //得到订单列表
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //list转map
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));


        //得到收货地址
        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId, shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);

        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {//判断该订单是否属于此用户
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<OrderVo> orderDetail(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);

        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }



    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {//判断该订单是否属于此用户
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //逻辑判断：只有[未付款]订单可以取消，根据业务需求判断
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {//付款了则不能取消了
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id:" + orderNo);
        }
        //只有[未付款]订单可以变成[已付款]，根据需求来
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())) {
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc() + "订单id:" + orderNo);
        }

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id:" + orderNo);
        }
    }




    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);//将order对象中的属性copy到orderVo

        List<OrderItemVo> OrderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());
        orderVo.setOrderItemVoList(OrderItemVoList);

        //地址判断
        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }




    private Order buildOrder(Integer uid,
                             Long orderNo,
                             Integer shippingId,
                             List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);//运费
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        return order;
    }


    /**
     * 可以用分布式唯一id/主键
     * 此处简单的时间戳
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNo);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }


    //4.1根据用户ID查询其所有订单
    @Override
    public ResponseVo<PageInfo<Order>> selectByUid(Integer uid,Integer currentPage, Integer pageSize) {

        PageHelper.startPage(currentPage,pageSize);
        List<Order> orderListByUid = orderMapper.selectByUid(uid);


        PageInfo pageInfo = new PageInfo<>(orderListByUid);
        pageInfo.setList(orderListByUid);
        return ResponseVo.success(pageInfo);
    }

    //4.2根据订单号查询订单
    @Override
    public ResponseVo<PageInfo<Order>> selectByOrderNo(Long orderNo,Integer currentPage, Integer pageSize) {

        PageHelper.startPage(currentPage,pageSize);
        List<Order> orderByOrderNo = orderMapper.selectByOrderNo02(orderNo);

        PageInfo pageInfo = new PageInfo<>(orderByOrderNo);
        pageInfo.setList(orderByOrderNo);

        return ResponseVo.success(pageInfo);
    }

    //查询所有订单
    @Override
    public ResponseVo<PageInfo<Order>> orderList(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<Order> orderList = orderMapper.selectAllOrder();

        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderList);
        return ResponseVo.success(pageInfo);
    }





}
