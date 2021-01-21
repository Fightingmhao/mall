package com.mhao.mall.service;

import com.github.pagehelper.PageInfo;
import com.mhao.mall.form.CategoryAddForm;
import com.mhao.mall.pojo.Category;
import com.mhao.mall.vo.ResponseVo;

import java.util.Set;

/**
 * Created by FightingHao on 2020/4/29
 */
public interface ICategoryService {

    ResponseVo<PageInfo> selectAll(Integer pageNum, Integer pageSize);

    void findSubCategoryId(Integer id, Set<Integer> resultSet);

    ResponseVo<Category> selectById(Integer id);

    ResponseVo<Category> insertCategory(Category category);

    ResponseVo deleteById(Integer id);

    ResponseVo update( Integer categoryId, CategoryAddForm categoryForm);

}
