package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Created by FightingHao on 2020/5/1
 */

@Data
public class OrderCreateForm {

    @NotNull
    private Integer shippingId;

}
