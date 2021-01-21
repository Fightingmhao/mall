package com.mhao.mall.controller;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.form.CategoryAddForm;
import com.mhao.mall.pojo.Category;
import com.mhao.mall.service.ICategoryService;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by FightingHao on 2020/4/29
 */
@RestController
public class CategoryController {


    @Autowired
    private ICategoryService categoryService;

    //查询所有商品分类
    @GetMapping("/categories")
    public ResponseVo<PageInfo> selectAll(@RequestParam(required = false, value = "currentPage", defaultValue = "1") Integer pageNum,
                                          @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize) {
        return categoryService.selectAll(pageNum, pageSize);
    }


    //添加分类
    @PostMapping("/categories/insertCategory")//（地址）
    public ResponseVo<Category> insertCategory(@Valid @RequestBody CategoryAddForm categoryAddForm){

        Category category = new Category();
        BeanUtils.copyProperties(categoryAddForm,category);//將categoryAddForm中内容copy到category中去
        //dto
        return categoryService.insertCategory(category);

    }

    //根据分类id删除分类
    @DeleteMapping("/categories")
    public ResponseVo delete(@RequestParam Integer categoryId) {
        return categoryService.deleteById(categoryId);
    }


    //根据分类id获取分类信息
    @GetMapping("/categories/{categoryId}")
    public ResponseVo<Category> category(@PathVariable Integer categoryId) {
        return categoryService.selectById(categoryId);
    }


    //根据商品分类id更新分类
    @PutMapping("/categories/{categoryId}")
    public ResponseVo update(@PathVariable Integer categoryId,
                             @Valid @RequestBody CategoryAddForm form) {
        return categoryService.update(categoryId, form);
    }









}
