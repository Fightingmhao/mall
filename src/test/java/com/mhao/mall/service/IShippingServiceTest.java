package com.mhao.mall.service;

import com.mhao.mall.MallApplicationTests;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.form.ShippingForm;
import com.mhao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 *
 * 收货地址相关操作
 * Created by FightingHao on 2020/4/30
 */

@Slf4j
public class IShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private ShippingForm form;

    private Integer shippingId;

    @Before
    public void before() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("孟浩");
        form.setReceiverAddress("河南周口");
        form.setReceiverCity("河南");
        form.setReceiverMobile("18888888888");
        form.setReceiverPhone("666666666");
        form.setReceiverProvince("河南");
        form.setReceiverDistrict("周口市");
        form.setReceiverZip("466300");
        this.form = form;

        add();
    }
//    @Test
    public void add() {

        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid, form);
        log.info("result={}", responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @After
    public void delete() {
//        Integer shippingId = 9;
        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("上海");
        ResponseVo responseVo = shippingService.update(uid, shippingId, form);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    //查询收货地址列表
    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid, 1, 10);
        log.info("result={}", responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}