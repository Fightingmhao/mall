package com.mhao.mall.service.impl;

import com.mhao.mall.MallApplicationTests;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.enums.RoleEnum;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.IUserService;
import com.mhao.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by FightingHao on 2020/3/5
 */

//@RunWith(SpringRunner.class)如果不继承测试主类（MallApplicationTests）就需要这两个注解
//@SpringBootTest

//该注解起到回滚的作用（让测试数据添加不到数据库中对数据造成污染)，Rolled back transaction for test
//@Transactional
public class UserServiceImplTest extends MallApplicationTests {

    public static final String USERNAME = "aaa1";
    public static final String PASSWORD = "aaa1";

    @Autowired
    private IUserService userService;

    @Test
    public void register() {
        User user = new User(USERNAME,PASSWORD,"aaa1@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.register(user);
    }

    @Test
    public void login(){
//        register();//必须先注册，才能登陆,可以用@Before注解来使register()方法先执行
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());//判断是否相等，括号里的前面是期待的参数0，后者是实际参数
    }

//    @Test
//    public void selectUsers() {
//        ResponseVo<List<UserVo>> responseVo = userService.userList();
//        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(), responseVo.getStatus());
//    }
}