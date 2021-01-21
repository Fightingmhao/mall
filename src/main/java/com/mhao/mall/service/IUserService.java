package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.pojo.User;
import com.mhao.mall.vo.ResponseVo;
import com.mhao.mall.vo.UserVo;

/**
 * Created by FightingHao on 2020/3/5
 */
public interface IUserService {

    /*
    * 注册
    * */
    ResponseVo<User> register(User user);



    /*
    * 登录
    * */
    ResponseVo<User> login(String username,String password);





    /*
     * 查询用户列表
     * */
    ResponseVo<PageInfo<UserVo>> userList(Integer currentPage, Integer pageSize);


    /*
     * 根据用户名查询用户
     */
    ResponseVo<PageInfo<UserVo>> selectByUsername(String username,Integer currentPage, Integer pageSize);
}
