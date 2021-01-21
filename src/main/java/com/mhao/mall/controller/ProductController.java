package com.mhao.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.form.ProductForm;
import com.mhao.mall.pojo.Product;
import com.mhao.mall.service.IProductService;
import com.mhao.mall.vo.ProductDetailVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/4/30
 */
@RestController
public class ProductController {

    @Autowired
    private IProductService productService;




    //1、查询所有商品信息（不传参数查询所有商品，传categoryId参数查询该分类下商品
    @GetMapping("/products")
    public ResponseVo<PageInfo> list(@RequestParam(required = false) Integer categoryId,
                                     @RequestParam(required = false, value = "currentPage", defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {
        return productService.list(categoryId, pageNum, pageSize);
    }

   /* //查询所有商品信息
    @GetMapping("/products/allProducts")
    public ResponseVo<PageInfo> allList(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        return productService.allList(pageNum, pageSize);
    }
*/


    //2、根据商品id查询该商品信息
    @GetMapping("/products/productById/{productId}")
    public ResponseVo<ProductDetailVo> detail(@PathVariable Integer productId) {
        return productService.detail(productId);
    }


    //3、添加商品
    @PostMapping("/products/addProduct")//（地址）
    public ResponseVo<Product> addProduct(@Valid @RequestBody ProductForm productForm){

        Product product = new Product();
        BeanUtils.copyProperties(productForm,product);//將productForm中内容copy到product中去

        return productService.addProduct(product);

    }


    //4、根据商品id删除商品
    @DeleteMapping("/products")
    public ResponseVo delete(@RequestParam Integer productId) {
        return productService.deleteById(productId);
    }


    //5、根据商品id更新修改商品
    @PutMapping("/products/updProductById")
    public ResponseVo updateById(@RequestParam Integer productId,
                             @Valid @RequestBody ProductForm productForm) {
        return productService.updateById(productId, productForm);
    }

    //上传文件
    @PostMapping("/upload")
    public ResponseVo upload1(@RequestParam("file") MultipartFile[] files) {
//        log.info("MultipartHttpServletRequest --- [{}]", files);
        return productService.upload1(files);
    }

}
