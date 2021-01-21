package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.form.ShippingForm;
import com.mhao.mall.vo.ResponseVo;

import java.util.Map;

/**
 * Created by FightingHao on 2020/4/30
 */
public interface IShippingService {

    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);

    ResponseVo delete(Integer uid, Integer shippingId);

    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);

    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);
}
