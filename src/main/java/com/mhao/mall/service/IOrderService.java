package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.pojo.Order;
import com.mhao.mall.vo.OrderVo;
import com.mhao.mall.vo.ResponseVo;

/**
 * Created by FightingHao on 2020/4/30
 */
public interface IOrderService {

    ResponseVo<OrderVo> create(Integer uid,Integer shippingId);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    ResponseVo<OrderVo> orderDetail(Long orderNo);

    ResponseVo cancel(Integer uid,Long orderNo);

    void paid(Long orderNo);

    ResponseVo<PageInfo<Order>> selectByUid(Integer uid,Integer currentPage, Integer pageSize);

    ResponseVo<PageInfo<Order>> selectByOrderNo(Long orderNo,Integer currentPage, Integer pageSize);


    ResponseVo<PageInfo<Order>> orderList(Integer currentPage, Integer pageSize);


}
