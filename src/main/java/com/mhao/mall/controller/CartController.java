package com.mhao.mall.controller;

import com.mhao.mall.consts.MallConst;
import com.mhao.mall.form.CartAddForm;
import com.mhao.mall.form.CartUpdateForm;
import com.mhao.mall.pojo.User;
import com.mhao.mall.service.ICartService;
import com.mhao.mall.vo.CartVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/4/30
 */
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    //获取购物车商品列表
    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.list(user.getId());
    }


    //添加商品到购物车
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm,HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);

        //return null;
    }

    //更新购物车（商品数量/是否选择）
    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm form,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(), productId, form);
    }

    //删除购物车中商品
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(), productId);
    }

    //全选
    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    //全不选
    @PutMapping("/carts/unSelectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    //获取商品总数
    @GetMapping("/carts/products/sum")
    public ResponseVo<Integer> sum(HttpSession session) {

        System.out.println("hahahahhahahh");
        System.out.println("++++++++++" + session.getAttribute(MallConst.CURRENT_USER));
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        System.out.println("--------------" + user.getId());
        return cartService.sum(user.getId());
    }
}
