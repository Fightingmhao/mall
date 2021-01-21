package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mhao.mall.MallApplicationTests;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.form.CartAddForm;
import com.mhao.mall.vo.CartVo;
import com.mhao.mall.vo.OrderVo;
import com.mhao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 订单列表及对应的订单项查询
 * Created by FightingHao on 2020/4/30
 */

@Slf4j
//@Transactional//测试类中的事务是为了避免数据库中出现脏数据，测试完后就回滚了
public class IOrderServiceTest extends MallApplicationTests {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private ICartService cartService;


    private Integer uid = 1;

    private Integer shippingId = 4;

    private Integer productId = 26;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();


    //先添加商品到购物车,可以在redis管理软件上看到
    @Before
    public void before() {
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid, form);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    //创建订单（下单），即将购物车中的商品下单，日志中还有收货地址信息,下单前必须先将商品添加到购物车并且选中
    @Test
    public void createTest(){
        ResponseVo<OrderVo> responseVo = create();
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    private ResponseVo<OrderVo> create() {//测试方法不能有返回值，故上面再写一个空返回值的调用带有返回值的

        ResponseVo<OrderVo> responseVo = orderService.create(uid,shippingId);
        log.info("result={}",gson.toJson(responseVo));
        return responseVo;

    }


    //查询订单及对应订单项
    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = orderService.list(uid, 1, 4);//第一页，四条订单
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


    //查询订单详情
    @Test
    public void detail() {
        ResponseVo<OrderVo> vo = create();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid, vo.getData().getOrderNo());
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    //取消订单（未付款的），即将订单状态update为0（已取消）
    @Test
    public void cancel() {
        ResponseVo<OrderVo> vo = create();
        System.out.println("订单创建了...");
        ResponseVo responseVo = orderService.cancel(uid, vo.getData().getOrderNo());
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
        System.out.println("订单取消了....");
    }

}