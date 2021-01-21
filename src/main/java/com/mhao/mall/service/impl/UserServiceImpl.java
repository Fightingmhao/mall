package com.mhao.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhao.mall.dao.UserMapper;
import com.mhao.mall.enums.RoleEnum;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.IUserService;
import com.mhao.mall.vo.ResponseVo;
import com.mhao.mall.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import static com.mhao.mall.enums.ResponseEnum.*;

/**
 * Created by FightingHao on 2020/3/5
 */

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /*
    * 注册
    * */

    @Override
    public ResponseVo<User> register(User user) {
      //  error();//用来模拟错误

        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if(countByUsername > 0){
//            throw new RuntimeException("该username已注册！");
            return ResponseVo.error(USERNAME_EXIST);//枚举
        }


        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if(countByEmail > 0){
//            throw new RuntimeException("该email已注册！");
            return ResponseVo.error(EMAIL_EXIST);
        }

        user.setRole(RoleEnum.CUSTOMER.getCode());//默认刚开始注册的用户都是普通用户

        //MD5加密（摘要算法Spring自带)
        user.setPassword(DigestUtils.md5DigestAsHex(
                user.getPassword().getBytes(StandardCharsets.UTF_8)));

        //写入数据库
        int resultCount = userMapper.insertSelective(user);
        if(resultCount == 0){
//            throw new RuntimeException("注册失败！");
            return ResponseVo.error(ERROR);
        }

        //注册成功
        return ResponseVo.success();
    }




    @Override
    public ResponseVo<User> login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        //用户名或密码错误
        if(user == null){
            //用户不存在(返回：用户名或密码错误）
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        if(!user.getPassword().equalsIgnoreCase(DigestUtils.md5DigestAsHex(
                password.getBytes(StandardCharsets.UTF_8)))){
            //和MD5加密后的密码比较且忽略大小写，如果密码不相等，则密码错误(返回：用户名或密码错误）
            return ResponseVo.error(USERNAME_OR_PASSWORD_ERROR);
        }

        user.setPassword("");//不需要返回密码,此处将密码设置为空
        return ResponseVo.success(user);
    }


    /*
    * 查询用户列表
    *
    * */
    @Override
    public ResponseVo<PageInfo<UserVo>> userList(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<User> userList = userMapper.selectAllUser();

        List<UserVo> UserVoList = userList.stream()
                .map(e -> {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyProperties(e, userVo);
                    return userVo;
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(userList);
        pageInfo.setList(UserVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<PageInfo<UserVo>> selectByUsername(String username,Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage,pageSize);
        List<User> user = userMapper.selectByUsername02(username);


        PageInfo pageInfo = new PageInfo<>(user);
        pageInfo.setList(user);
        return ResponseVo.success(pageInfo);
    }

//    @Override
//    public ResponseVo<List<UserVo>> userList() {
//
//        List<UserVo> userVoList = UserMapper.selectAllUserList();
//        return ResponseVo.success(userVoList);
//    }



    //模拟错误
    private void error(){
        throw new RuntimeException("意外错误");
    }
}
