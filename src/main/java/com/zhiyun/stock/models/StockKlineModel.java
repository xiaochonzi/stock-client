package com.zhiyun.stock.models;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: StockKlineModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/8 10:03
 **/
@Data
public class StockKlineModel {
    private Date date;
    private String time;
    private String code;
    private String name;
    private BigDecimal close;
    private BigDecimal high;
    private BigDecimal open;
    private BigDecimal low;
    private BigDecimal volumn;
    private BigDecimal volumnPrice;
    private BigDecimal startVolumn;
}
