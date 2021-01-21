package com.mhao.mall;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置拦截器
 * Created by FightingHao on 2020/3/5
 */
//配置拦截器
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Value("${upload.upload-folder}")
    private String uploadFolder;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {//
        registry.addInterceptor(new UserLoginInterceptor())//注册拦截器
                .addPathPatterns("/**")//默认对所有的Url进行拦截
                .excludePathPatterns("/upload","/img/**","/error","/orders/orderDetail","/orders/selectAll","/orders/selectByUid","/orders/selectByOrderNo","/user/*","/categories","/categories/*","/products","/products/productById/*","/products/*");//除了这几个标注的
    }

    /*
    * 前端访问本地资源放行
    *
    * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + uploadFolder);
    }




}
