package com.mhao.mall.dao;

import com.mhao.mall.pojo.Securities_Info;

import java.util.List;

public interface Securities_InfoMapper {

    List<Securities_Info> selectAll();

    int deleteByPrimaryKey(Integer id);

    int insert(Securities_Info record);

    int insertSelective(Securities_Info record);

    Securities_Info selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Securities_Info record);

    int updateByPrimaryKey(Securities_Info record);
}