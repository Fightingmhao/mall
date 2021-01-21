package com.mhao.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhao.mall.dao.ProductMapper;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.form.ProductForm;
import com.mhao.mall.pojo.Product;
import com.mhao.mall.service.ICategoryService;
import com.mhao.mall.service.IProductService;
import com.mhao.mall.utils.UUIDUtils;
import com.mhao.mall.vo.ProductDetailVo;
import com.mhao.mall.vo.ProductVo;
import com.mhao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mhao.mall.enums.ProductStatusEnum.DELETE;
import static com.mhao.mall.enums.ProductStatusEnum.OFF_SALE;
import static com.mhao.mall.enums.ResponseEnum.ERROR;
import static com.mhao.mall.enums.ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE;

/**
 * Created by FightingHao on 2020/4/29
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {


    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Value("${upload.upload-folder}")
    private String uploadFolder;



    //1、查询商品列表
    @Override
    public ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize) {

        Set<Integer> categoryIdSet = new HashSet<>();
        if (categoryId != null){
            categoryService.findSubCategoryId(categoryId,categoryIdSet);
            categoryIdSet.add(categoryId);
        }

        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList = productMapper.selectByCategoryIdSet(categoryIdSet);
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }

    /*@Override
    public ResponseVo<PageInfo> allList(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);

        List<Product> productList = productMapper.selectByCategoryIdSet();
        List<ProductVo> productVoList = productList.stream()
                .map(e -> {
                    ProductVo productVo = new ProductVo();
                    BeanUtils.copyProperties(e, productVo);
                    return productVo;
                })
                .collect(Collectors.toList());

        PageInfo pageInfo = new PageInfo<>(productList);
        pageInfo.setList(productVoList);
        return ResponseVo.success(pageInfo);
    }*/

    //2、根据分类id查询该分类下所有商品

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
        Product product = productMapper.selectByPrimaryKey(productId);

        //只对确定性条件判断,避免以后再填加商品状态（比如大甩卖）时出错
        if (product.getStatus().equals(OFF_SALE.getCode())
                || product.getStatus().equals(DELETE.getCode())) {
            return ResponseVo.error(PRODUCT_OFF_SALE_OR_DELETE);
        }

        ProductDetailVo productDetailVo = new ProductDetailVo();
        BeanUtils.copyProperties(product, productDetailVo);
        //敏感数据处理,如果库存大于100就显示100，小于的话就显示本身真实数据
        productDetailVo.setStock(product.getStock() > 100 ? 100 : product.getStock());
        return ResponseVo.success(productDetailVo);
    }

    //3、添加商品
    @Override
    public ResponseVo<Product> addProduct(Product product) {
        if(product.getId() == null){//若商品id为空，添加商品到数据库

            int resultCount = productMapper.insertSelective(product);
            if(resultCount == 0){
//            throw new RuntimeException("添加失败！");
                return ResponseVo.error(ERROR);
            }
        }else{//商品id不为空，执行更新方法
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row == 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }


        //添加成功
        return ResponseVo.success();
    }

    //4、删除商品（根据id）
    @Override
    public ResponseVo deleteById(Integer id) {
        int resultCount = productMapper.deleteByPrimaryKey(id);
        if(resultCount == 0){
//            throw new RuntimeException("删除失败！");
            return ResponseVo.error(ERROR);
        }

        return ResponseVo.success();
    }


    //5、更新商品（根据id）
    @Override
    public ResponseVo updateById(Integer productId, ProductForm productForm) {
        Product product = new Product();
        BeanUtils.copyProperties(productForm, product);//将获取到的form表单复制到product
        product.setId(productId);
        int row = productMapper.updateByPrimaryKeySelective(product);
        if (row == 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }


    @Override
    public ResponseVo upload1(MultipartFile[] files) {
        StringBuffer buffer = new StringBuffer();
        if (files !=null && files.length > 0) {
            for (MultipartFile multipartFile: files) {
                System.out.println("1 ===> " + multipartFile.getName());
                System.out.println("1 ===> " + multipartFile.getOriginalFilename());
                //获取文件的名字
                String originalFilename = multipartFile.getOriginalFilename();
                //获取文件名的后缀
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                //生成新的文件名
                String newFileName = UUIDUtils.getUUID() + suffix;
                System.out.println("newFileName -- " + newFileName);
                //创建文件夹
                File file = new File(uploadFolder + newFileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //上传
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String picUrl = "http://localhost:8081/img/" + newFileName;
                System.out.println(picUrl);
                buffer.append(picUrl + ",");
                System.out.println("buffer   " + buffer);
            }
            buffer.deleteCharAt(buffer.length() - 1);
            System.out.println("buffer   " + buffer);
        }
        return ResponseVo.success(buffer);
    }








}
