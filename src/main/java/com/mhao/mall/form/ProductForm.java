package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by FightingHao on 2020/5/10
 */

@Data
public class ProductForm {

    //@NotBlank//用于string 来判断是否为空，是的话则非法
    //@NotEmpty 用于集合
    //@NotNull  判断是否为空

    private Integer id;

    @NotNull
    private Integer categoryId;

    @NotBlank
    private String name;

    @NotBlank
    private String subtitle;

    @NotBlank
    private String mainImage;

    @NotBlank
    private String subImages;

    @NotBlank
    private String detail;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer stock;

    @NotNull
    private Integer status;












}
