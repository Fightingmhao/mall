package com.mhao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by FightingHao on 2020/4/30
 */
@Data
public class CartVo {

    private List<CartProductVo> cartProductVoList;

    private Boolean selectedAll;

    private BigDecimal cartTotalPrice;

    private Integer cartTotalQuantity;

}
