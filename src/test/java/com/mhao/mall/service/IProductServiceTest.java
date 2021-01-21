package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.MallApplicationTests;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.vo.ProductDetailVo;
import com.mhao.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 商品列表详情
 * Created by FightingHao on 2020/4/30
 */
public class IProductServiceTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list() {
//        ResponseVo<PageInfo> responseVo = productService.list(null, 2, 3);
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
        ResponseVo<PageInfo> responseVo = productService.list(null, 1, 2);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }

    @Test
    public void detail() {
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
    }
}