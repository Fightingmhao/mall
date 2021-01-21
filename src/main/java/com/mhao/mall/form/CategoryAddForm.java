package com.mhao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by FightingHao on 2020/5/10
 */

@Data
public class CategoryAddForm {

    private Integer id;

    @NotNull
    private Integer parentId;

    @NotBlank
    private String name;

    @NotNull
    private Integer status;

    @NotNull
    private Integer sortOrder;


}
