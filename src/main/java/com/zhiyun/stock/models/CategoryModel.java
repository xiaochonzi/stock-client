package com.zhiyun.stock.models;

import lombok.Data;

/**
 * @program: HangyeModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 22:43
 **/
@Data
public class CategoryModel {

    private String id;
    private String title;
    private String parentId;
}
