package com.mhao.mall.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/*
* 此处对于PayInfo正确做法应该是：pay项目提供client.jar,mall项目引入jar包，而不是在mall项目中新建一个（从pay项目copy来）PayInfo
*
* */

@Data
public class PayInfo {
    private Integer id;

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

    private BigDecimal payAmount;

    private Date createTime;

    private Date updateTime;

    public PayInfo(Long orderNo, Integer payPlatform, String platformStatus, BigDecimal payAmount) {
        this.orderNo = orderNo;
        this.payPlatform = payPlatform;
        this.platformStatus = platformStatus;
        this.payAmount = payAmount;
    }
}