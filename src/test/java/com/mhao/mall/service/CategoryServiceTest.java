package com.mhao.mall.service;

import com.mhao.mall.MallApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * 查询商品分类信息
 * Created by FightingHao on 2020/4/29
 */

@Slf4j
public class CategoryServiceTest extends MallApplicationTests {

    @Autowired
    private ICategoryService categoryService;

//    @Test
//    public void selectAll() {
//        ResponseVo<List<CategoryVo>> responseVo = categoryService.selectAll();
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
//    }

    @Test
    public void findSubCategoryId() {
        Set<Integer> set = new HashSet<>();
        categoryService.findSubCategoryId(100002, set);
        log.info("set={}", set);
    }
}