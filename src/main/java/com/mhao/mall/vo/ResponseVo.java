package com.mhao.mall.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mhao.mall.enums.ResponseEnum;
import lombok.Data;
import org.springframework.validation.BindingResult;

import java.util.Objects;

/**
 * Created by FightingHao on 2020/3/5
 *
 * 用来配置响应的返回信息
 */

@Data
//序列化配置
//@JsonSerialize(include = )/旧版本
@JsonInclude(value = JsonInclude.Include.NON_NULL)//可将空字段不反回，新版
public class ResponseVo<T> {

    private Integer status;

    private String msg;

    private T data;

    private ResponseVo(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }
    private ResponseVo(Integer status, T data) {
        this.status = status;
        this.data = data;
    }

    //成功的静态方法，用来调用
    public static <T> ResponseVo<T> successByMsg(String msg){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),msg);
    }
    public static <T> ResponseVo<T> success(T data){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),data);
    }
    public static <T> ResponseVo<T> success(){
        return new ResponseVo<>(ResponseEnum.SUCCESS.getCode(),ResponseEnum.SUCCESS.getDesc());
    }
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum){
        return new ResponseVo<>(responseEnum.getCode(),responseEnum.getDesc());
    }
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum,String msg){
        return new ResponseVo<>(responseEnum.getCode(),msg);
    }
    public static <T> ResponseVo<T> error(ResponseEnum responseEnum, BindingResult bindingResult){
        return new ResponseVo<>(responseEnum.getCode(),
                Objects.requireNonNull(bindingResult.getFieldError()).getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }



}
