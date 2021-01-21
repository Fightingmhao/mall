package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 添加商品表单
 * Created by FightingHao on 2020/4/30
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;

    private Boolean selected = true;
}