package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by FightingHao on 2020/3/5
 */

//新建的对象（form包）专门用来接收controller传来的参数
@Data
public class UserLoginForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;


}
