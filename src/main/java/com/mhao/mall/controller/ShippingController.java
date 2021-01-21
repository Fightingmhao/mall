package com.mhao.mall.controller;

import com.mhao.mall.consts.MallConst;
import com.mhao.mall.form.ShippingForm;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.IShippingService;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/4/30
 */


@RestController
public class ShippingController {

    @Autowired
    private IShippingService shippingService;

    //为当前用户添加地址
    @PostMapping("/shippings")
    public ResponseVo add(@Valid @RequestBody ShippingForm form,
                          HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.add(user.getId(), form);
    }

    //根据当前用户的其中一个地址id删除地址
    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable Integer shippingId,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.delete(user.getId(), shippingId);
    }

    //根据当前用户的一个地址id更新地址
    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable Integer shippingId,
                             @Valid @RequestBody ShippingForm form,
                             HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.update(user.getId(), shippingId, form);
    }

    //获取当前用户的所有地址/列表
    @GetMapping("/shippings")
    public ResponseVo list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                           @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                           HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}