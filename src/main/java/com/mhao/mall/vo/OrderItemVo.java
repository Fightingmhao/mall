package com.mhao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by FightingHao on 2020/4/30
 */
@Data
public class OrderItemVo {

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;

    private Date createTime;
}

