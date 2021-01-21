package com.mhao.mall.listener;

import com.google.gson.Gson;
import com.mhao.mall.pojo.PayInfo;
import com.mhao.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by FightingHao on 2020/5/1
 *
 * 对于PayInfo正确做法应该是：pay项目提供client.jar,   mall项目引入jar包，而不是在mall项目中新建一个（从pay项目copy来）PayInfo
 */

@Component
@RabbitListener(queues = "payNotify")
@Slf4j
public class PayMsgListener {

    @Autowired
    private IOrderService orderService;

    //接收监听到的消息
    @RabbitHandler
    public void process(String msg){
        log.info("【接受到消息 => 】{}",msg);

        PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
        if (payInfo.getPlatformStatus().equals("SUCCESS")){
            //修改订单状态
            orderService.paid(payInfo.getOrderNo());
        }


    }

}
