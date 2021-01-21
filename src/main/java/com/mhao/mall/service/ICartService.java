package com.mhao.mall.service;

import com.mhao.mall.form.CartAddForm;
import com.mhao.mall.form.CartUpdateForm;
import com.mhao.mall.pojo.Cart;
import com.mhao.mall.vo.CartVo;
import com.mhao.mall.vo.ResponseVo;

import java.util.List;

/**
 * Created by FightingHao on 2020/4/30
 */
public interface ICartService {


//添加商品到购物车
    ResponseVo<CartVo> add(Integer uid,CartAddForm form);
//查询商品列表
    ResponseVo<CartVo> list(Integer uid);
//更新购物车
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);
//删除购物车
    ResponseVo<CartVo> delete(Integer uid, Integer productId);
//全选
    ResponseVo<CartVo> selectAll(Integer uid);
//全不选
    ResponseVo<CartVo> unSelectAll(Integer uid);
//总数量
    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);



}
