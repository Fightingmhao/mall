package com.mhao.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.consts.MallConst;
import com.mhao.mall.form.OrderCreateForm;
import com.mhao.mall.pojo.Order;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.IOrderService;
import com.mhao.mall.vo.OrderVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/5/1
 */


@RestController
public class OrderController {

    @Autowired
    private IOrderService orderService;


    //创建订单（下单），购物车中必须有商品并且是选中状态，传入的参数是收货地址shippingId如："shippingId":"4"
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm form,
                                      HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.create(user.getId(), form.getShippingId());
    }

    //获取当前用户的订单列表
    @GetMapping("/orders")
    public ResponseVo<PageInfo> list(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.list(user.getId(), pageNum, pageSize);
    }

    //查看订单详情
    @GetMapping("/orders/{orderNo}")
    public ResponseVo<OrderVo> detail(@PathVariable Long orderNo,
                                      HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }

    //后台管理查看订单项详情
    @GetMapping("/orders/orderDetail")
    public ResponseVo<OrderVo> orderDetail(@RequestParam Long orderNo) {

        return orderService.orderDetail(orderNo);
    }




    //取消订单
    @PutMapping("/orders/{orderNo}")
    public ResponseVo cancel(@PathVariable Long orderNo,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }


    //4.1根据用户ID查询其所有订单
    @GetMapping("/orders/selectByUid")
    public ResponseVo<PageInfo<Order>> selectByUid(@RequestParam Integer uid,
                                                   @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                                                   @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize) {
        return orderService.selectByUid(uid,currentPage, pageSize);
    }

    //4.2根据订单号查询订单
    @GetMapping("/orders/selectByOrderNo")
    public ResponseVo<PageInfo<Order>> selectByOrderNo(@RequestParam Long orderNo,
                                             @RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize) {
        return orderService.selectByOrderNo(orderNo,currentPage, pageSize);
    }

    //查询所有订单
    @GetMapping("/orders/selectAll")
    public ResponseVo<PageInfo<Order>> selectAll(@RequestParam(value = "currentPage",defaultValue = "1") Integer currentPage,
                                                 @RequestParam(value = "pageSize",defaultValue = "5") Integer pageSize){
        return orderService.orderList(currentPage, pageSize);
    }




}