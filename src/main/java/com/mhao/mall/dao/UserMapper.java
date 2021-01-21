package com.mhao.mall.dao;

import com.mhao.mall.pojo.User;
import com.mhao.mall.vo.UserVo02;

import java.util.List;

public interface UserMapper {

    List<User> selectAllUser();

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    int countByUsername(String username);

    int countByEmail(String email);

    User selectByUsername(String username);

    List<User> selectByUsername02(String username);


    String sqlTest(UserVo02 userVo02);

}