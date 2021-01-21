package com.mhao.mall.dao;

import com.mhao.mall.pojo.Order;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUid(Integer uid);

    Order selectByOrderNo(Long orderNo);

    List<Order> selectByOrderNo02(Long orderNo);

    List<Order> selectAllOrder();
}