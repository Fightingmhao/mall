package com.mhao.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhao.mall.dao.Securities_InfoMapper;
import com.mhao.mall.pojo.Securities_Info;
import com.mhao.mall.service.InfoService;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by FightingHao on 2020/3/5
 */

@Service
public class InfoServiceImpl implements InfoService {

    @Autowired
    private Securities_InfoMapper securities_InfoMapper ;



    /*
    * 查询用户列表
    *
    * */
    @Override
    public ResponseVo<PageInfo<Securities_Info >> userList(Integer currentPage, Integer pageSize) {
        PageHelper.startPage(currentPage, pageSize);
        List<Securities_Info> userList = securities_InfoMapper.selectAll();



        PageInfo pageInfo = new PageInfo<>(userList);
        return ResponseVo.success(pageInfo);
    }




    //模拟错误
    private void error(){
        throw new RuntimeException("意外错误");
    }
}
