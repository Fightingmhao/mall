package com.mhao.mall.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by FightingHao on 2020/4/29
 */

@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Boolean status;

    private Integer sortOrder;

    private Date createTime;

    private List<CategoryVo> subCategories;

}
