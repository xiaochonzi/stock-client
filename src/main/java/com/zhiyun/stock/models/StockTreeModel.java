package com.zhiyun.stock.models;

import lombok.Data;

import java.util.List;

/**
 * @program: StockTreeModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 23:12
 **/
@Data
public class StockTreeModel {
    private String stockCode;
    private Boolean stopFlag;
    private List<String> typeTree;
    private List<String> typeIds;
    private Integer type;
}
