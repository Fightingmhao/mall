package com.mhao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by FightingHao on 2020/4/29
 */

@Data
public class ProductVo {


    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private Integer status;

    private BigDecimal price;

    private Integer stock;

}
