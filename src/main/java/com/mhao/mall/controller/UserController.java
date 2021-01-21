package com.mhao.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.consts.MallConst;
import com.mhao.mall.form.UserLoginForm;
import com.mhao.mall.form.UserRegisterForm;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.IUserService;
import com.mhao.mall.vo.ResponseVo;
import com.mhao.mall.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/3/5
 */

@RestController
@Slf4j//打印日志
public class UserController {

//  post请求第一种方式1,获取前端传过来的参数，前端以x-www-form-urlencoded方式，后端以@RequestParam方式或2接收
   /* @PostMapping("/register")
    public void register(@RequestParam(value = "username") String userName){
        log.info("username={}",userName);//以打印日志方式输出在控制台不要用system.out.println
    }
    */
//  post请求第一种方式2
    /*@PostMapping("/register")
    public void register(User user) {
        log.info("username={}", user.getUsername());//以打印日志方式输出在控制台不要用system.out.println
    }*/

    @Autowired
    private IUserService userService;


/*
* 注册
*
* */
//   第二种方式，前端以raw，JSON方式传，后端以@RequestBody方式接收
    @PostMapping("/user/register")//（地址）
    public ResponseVo<User> register(@Valid @RequestBody UserRegisterForm userForm){
        //是否有错误，如果有则执行,错误被拦截了
        /*if(bindingResult.hasErrors()){
            log.error("注册提交的参数有误，{} {}",
                    Objects.requireNonNull(bindingResult.getFieldError()).getField(),//让提示更加清楚
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }*/

        User user = new User();
        BeanUtils.copyProperties(userForm,user);//將userForm中内容copy到user中去
        //dto
        return userService.register(user);

       /* log.info("username={}",userForm.getUsername());//以打印日志方式输出在控制台不要用system.out.println
//        return ResponseVo.success();
        return ResponseVo.error(NEED_LOGIN);*/
    }



/*
* 登录
*
* */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  HttpSession session){
        /*if(bindingResult.hasErrors()){
            *//*日志log.error("注册提交的参数有误，{} {}",
                    bindingResult.getFieldError().getField(),//让提示更加清楚
                    bindingResult.getFieldError().getDefaultMessage());*//*
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }*/

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());

        //设置session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());//setAttribute
        log.info("/login sessionId={}",session.getId());//打印sessionId
        return userResponseVo;

    }

/*
* 获取登录用户信息session.getAttribute
* */
//session保存在内存里
    @GetMapping("/user")//两个方法一起用才能测试出来登录效果
    public ResponseVo<User> userInfo(HttpSession session){
        log.info("/user sessionId={}",session.getId());
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取到sessionId后，从中拿数据getAttribute
        /*if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);

        }*/
        return ResponseVo.success(user);
    }


/*
* 退出登录
*
* */
    //TODO 判断登录状态，拦截器(统一在一个地方对用户登录状态进行判断，第一种：Interceptor ---Url,第二种：AOP ---包名
    /*
    * {@link TomcatServletWebServerFactory } getSessionTimeoutInMinutes
    *
    * */
    @PostMapping("/user/logout")
    public ResponseVo logout(HttpSession session){
        log.info("/User/logout sessionId={}",session.getId());
        /*//以下四行用来判断是否是登录状态，使用拦截器就不用判断了
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);//获取到sessionId后，从中拿数据getAttribute
        if(user == null){
            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }*/
        session.removeAttribute(MallConst.CURRENT_USER);
        System.out.println("$$$$$$$"+ResponseVo.success());
        return ResponseVo.success();
    }

    //获取用户列表
    @GetMapping("/user/selectUser")
    public ResponseVo<PageInfo<UserVo>> list(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return userService.userList(currentPage, pageSize);
    }

    //根据用户名查询用户
    @GetMapping("/user/selectByUsername")
    public ResponseVo<PageInfo<UserVo>> selectByUsername(@RequestParam String username,
                                             @RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return userService.selectByUsername(username,currentPage, pageSize);
    }

}
