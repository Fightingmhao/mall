package com.mhao.mall.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by FightingHao on 2020/4/30
 */
@Data
public class UserVo {


    private Integer id;

    private String username;

    private String email;

    private String phone;

    private Date createTime;

    private Date updateTime;



//    private List<UserVo> subUsers;


}
