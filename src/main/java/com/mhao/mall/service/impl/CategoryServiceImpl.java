package com.mhao.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mhao.mall.dao.CategoryMapper;
import com.mhao.mall.enums.ResponseEnum;
import com.mhao.mall.form.CategoryAddForm;
import com.mhao.mall.pojo.Category;
import com.mhao.mall.service.ICategoryService;
import com.mhao.mall.vo.CategoryVo;
import com.mhao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mhao.mall.consts.MallConst.ROOT_PARENT_ID;
import static com.mhao.mall.enums.ResponseEnum.ERROR;

/**
 *
 * 耗时：http请求(如请求微信api) > 磁盘 > 内存
 *      mysql(内网+磁盘)
 *
 * Created by FightingHao on 2020/4/29
 */
@Service
public class CategoryServiceImpl implements ICategoryService {


    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public  ResponseVo<PageInfo> selectAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Category> categories = categoryMapper.selectAll();

        //查出parent_id=0
//		for (Category category : categories) {
//			if (category.getParentId().equals(ROOT_PARENT_ID)) {
//				CategoryVo categoryVo = new CategoryVo();
//				BeanUtils.copyProperties(category, categoryVo);
//				categoryVoList.add(categoryVo);
//			}
//		}

        //lambda + stream
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())
                .collect(Collectors.toList());

        //查询子目录
        findSubCategory(categoryVoList, categories);

        PageInfo pageInfo = new PageInfo<>(categories);
        pageInfo.setList(categoryVoList);
        return ResponseVo.success(pageInfo);
    }

    //查询子目录方法
    private void findSubCategory(List<CategoryVo> categoryVoList, List<Category> categories) {
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();

            for (Category category : categories) {
                //如果查到内容，设置子目录subCategory, 继续往下查，查到一级目录查二级目录，一直查到最后
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }

                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                categoryVo.setSubCategories(subCategoryVoList);

                //递归调用，继续往下查子目录
                findSubCategory(subCategoryVoList, categories);
            }
        }
    }

    /*
    * 根据id查询子分类
    * */

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id, resultSet, categories);
    }


    /*
    * 根据分类id查询分类
    * */
    @Override
    public ResponseVo<Category> selectById(Integer id) {
        Category category = categoryMapper.selectByPrimaryKey(id);


        return ResponseVo.success(category);
    }


    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(), resultSet, categories);
            }
        }
    }

//转换返回categoryVo对象
    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category, categoryVo);
        return categoryVo;
    }

    /*
     * 添加/修改分类
     *
     * */

    @Override
    public ResponseVo<Category> insertCategory(Category category) {
        if(category.getId() == null){//id为空时，添加分类
            int resultCount = categoryMapper.insertSelective(category);
            if(resultCount == 0){
//            throw new RuntimeException("添加失败！");
                return ResponseVo.error(ERROR);
            }
        }else {//id不为空，修改商品分类
            int row = categoryMapper.updateByPrimaryKeySelective(category);
            if (row == 0) {
                return ResponseVo.error(ResponseEnum.ERROR);
            }
        }


        return ResponseVo.success();
    }

    @Override
    public ResponseVo deleteById(Integer id) {
        int resultCount = categoryMapper.deleteByPrimaryKey(id);
        if(resultCount == 0){
//            throw new RuntimeException("删除失败！");
            return ResponseVo.error(ERROR);
        }

        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer categoryId, CategoryAddForm categoryForm) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryForm, category);//将获取到的form表单复制到category
        category.setId(categoryId);
        int row = categoryMapper.updateByPrimaryKeySelective(category);
        if (row == 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

}
