package com.zhiyun.stock.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: CategoryStockModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/10 17:44
 **/
@Data
public class CategoryStockModel {
    private BigDecimal totalValues;
    private List<String> stockCodes;
}
