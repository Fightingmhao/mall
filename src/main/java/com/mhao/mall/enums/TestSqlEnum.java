package com.mhao.mall.enums;

import lombok.Getter;

/**
 * Created by FightingHao on 2020/3/5
 */

@Getter
public enum TestSqlEnum {

    PHONE("phone","select phone from mall_user where username = #{username}"),
    EMAIL("email","select email from mall_user where username = #{username}"),
    ;

    String code;
    String desc;
    TestSqlEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
