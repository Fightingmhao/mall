package com.mhao.mall;

import com.mhao.mall.consts.MallConst;
import com.mhao.mall.exception.UserLoginException;
import com.mhao.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 * Created by FightingHao on 2020/3/5
 */
//新建拦截器，如果比较多的话 就见一个包来专门存
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /*
    * true 表示继续流程   false表示中断
    * */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        User user = (User) request.getSession().getAttribute(MallConst.CURRENT_USER);//获取到sessionId后，从中拿数据getAttribute
        if(user == null){
            log.info("user=null");
            throw new UserLoginException();//方法二：通过抛出异常来让统一异常处理来捕获，从而得到返回值
//            response.getWriter().print("error");方法一：当被拦截后若是需要返回一些内容，可用此方法（不推荐），因为下面的return已经写好了返回内容
//            return false;
//            return ResponseVo.error(ResponseEnum.NEED_LOGIN);
        }
        return true;
    }
}
