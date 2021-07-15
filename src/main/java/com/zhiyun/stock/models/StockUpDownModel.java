package com.zhiyun.stock.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: StockUpDownModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/10 17:08
 **/
@Data
public class StockUpDownModel implements Comparable {
    @ApiModelProperty("股票代码")
    private String code;
    @ApiModelProperty("股票名称")
    private String name;
    @ApiModelProperty("涨幅比率")
    private BigDecimal changeRate;

    @Override
    public int compareTo(Object o) {
        return ((StockUpDownModel)o).changeRate.compareTo(changeRate);
    }
}
