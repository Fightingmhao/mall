package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.form.ProductForm;
import com.mhao.mall.pojo.Product;
import com.mhao.mall.vo.ProductDetailVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by FightingHao on 2020/4/29
 */
public interface IProductService {

    //1、查询所有商品信息（不传参数查询所有商品，传categoryId参数查询该分类下商品
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    /*//查询所有商品信息
    ResponseVo<PageInfo> allList(Integer pageNum, Integer pageSize);*/

    //2、根据商品id查询该商品信息
    ResponseVo<ProductDetailVo> detail(Integer productId);

    //3、添加商品
    ResponseVo<Product> addProduct(Product product);

    //4、删除商品（根据id）
    ResponseVo deleteById(Integer id);

    //5、更新商品（根据id）
    ResponseVo updateById( Integer productId, ProductForm productForm);

    //上传商品图片
    ResponseVo upload1(MultipartFile[] files);
}
