package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.pojo.Securities_Info;
import com.mhao.mall.vo.ResponseVo;

/**
 * Created by FightingHao on 2020/3/5
 */
public interface InfoService {



    /*
     * 查询用户列表
     * */
    ResponseVo<PageInfo<Securities_Info >> userList(Integer currentPage, Integer pageSize);


}
