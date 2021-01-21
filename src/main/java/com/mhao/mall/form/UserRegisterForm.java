package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Created by FightingHao on 2020/3/5
 */

//新建的对象（form包）专门用来接收controller传来的参数
@Data
public class UserRegisterForm {

   //@NotBlank//用于string 来判断是否为空格，是的话则非法
   //@NotEmpty 用于集合
   //@NotNull  判断是否为空

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String email;

    @NotBlank
    private String phone;
}
