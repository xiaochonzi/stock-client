package com.zhiyun.stock.models;

import lombok.Data;

/**
 * @program: StockSearchModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 23:33
 **/
@Data
public class StockSearchModel {
    private String code;
    // 拼音简写
    private String namePrefix;
    // 名称
    private String name;
    // 类型：0-》个股；1—>指数
    private Integer type;
}
