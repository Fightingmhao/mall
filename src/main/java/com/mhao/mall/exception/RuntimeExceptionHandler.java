package com.mhao.mall.exception;

import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Objects;

import static com.mhao.mall.enums.ResponseEnum.ERROR;

/**
 * Created by FightingHao on 2020/3/5
 */
//用来捕获异常
@ControllerAdvice
public class RuntimeExceptionHandler {
    //捕获异常
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody//要返回json格式需要加这个注解
//    @ResponseStatus(HttpStatus.FORBIDDEN)  http状态码，与返回内容无关
    public ResponseVo handle(RuntimeException e){
        return ResponseVo.error(ERROR,e.getMessage());
    }

    //捕获异常
    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle(){
        return ResponseVo.error(ResponseEnum.NEED_LOGIN);
    }



    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo notValidExceptionHandle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        Objects.requireNonNull(bindingResult.getFieldError());
        return ResponseVo.error(ResponseEnum.PARAM_ERROR,
                bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage());
    }
}
