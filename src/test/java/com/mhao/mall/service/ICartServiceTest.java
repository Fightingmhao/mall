package com.mhao.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mhao.mall.MallApplicationTests;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.form.CartAddForm;
import com.mhao.mall.form.CartUpdateForm;
import com.mhao.mall.vo.CartVo;
import com.mhao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 购物车模块
 * Created by FightingHao on 2020/4/30
 */

@Slf4j
public class ICartServiceTest extends MallApplicationTests {

    @Autowired
    private ICartService cartService;

    //用来格式化成json
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Integer uid = 1;

    private Integer productId = 28;

    @Test
    public void add() {

        log.info("【新增购物车...】");
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(uid,form);
        log.info("list={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void list(){

        ResponseVo<CartVo> responseVo = cartService.list(uid);
        log.info("list={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());

    }

    @Test
    public void update() {
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
        form.setSelected(false);
        ResponseVo<CartVo> responseVo = cartService.update(uid, productId, form);
        log.info("result={}", gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }


    @Test
    public void delete() {
        log.info("【删除购物车...】");
        ResponseVo<CartVo> responseVo = cartService.delete(uid, productId);
        log.info("result={}", gson.toJson(responseVo));
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = cartService.selectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void unSelectAll() {
        ResponseVo<CartVo> responseVo = cartService.unSelectAll(uid);
        log.info("result={}", gson.toJson(responseVo));
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void sum() {
        ResponseVo<Integer> responseVo = cartService.sum(uid);
        log.info("result={}", gson.toJson(responseVo));
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}