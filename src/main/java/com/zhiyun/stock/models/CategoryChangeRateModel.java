package com.zhiyun.stock.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: CategoryChangeRateModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/9 14:59
 **/
@Data
@ApiModel("板块涨幅")
public class CategoryChangeRateModel {
    // 板块id
    @ApiModelProperty("板块id")
    private String id;
    // 板块标题
    @ApiModelProperty("板块标题")
    private String title;
    // 板块涨幅
    @ApiModelProperty("板块涨幅比率")
    private BigDecimal rate;
    // 领涨股code
    @ApiModelProperty("领涨股代码")
    private String code;
    // 领涨股名称
    @ApiModelProperty("领涨幅名称")
    private String name;
    // 当前股价
    @ApiModelProperty("当前股价")
    private BigDecimal price;
    // 当前涨幅
    @ApiModelProperty("当前涨幅")
    private BigDecimal changeRate;
}
