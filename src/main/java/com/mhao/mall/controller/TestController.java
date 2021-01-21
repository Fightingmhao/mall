package com.mhao.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.pojo.Securities_Info;
import com.mhao.mall.service.InfoService;
import com.mhao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by FightingHao on 2020/3/5
 */

@RestController
@Slf4j//打印日志
public class TestController {



    @Autowired
    private InfoService infoService;



    //获取用户列表
    @GetMapping("/user/selectInfo")
    public ResponseVo<PageInfo<Securities_Info >> list(@RequestParam(value = "currentPage", defaultValue = "1") Integer currentPage,
                                                       @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        return infoService.userList(currentPage, pageSize);
    }


}
