package com.mhao.mall.enums;

import lombok.Getter;

/**
 * Created by FightingHao on 2020/4/30
 */

@Getter
public enum PaymentTypeEnum {

    PAY_ONLINE(1),
      ;

    Integer code;

    PaymentTypeEnum(Integer code){
        this.code = code;
    }
}
