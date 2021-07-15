package com.zhiyun.stock.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * @program: StockRealTimeModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 23:49
 **/
@Data
@ApiModel("实时行情")
public class StockRealTimeModel {

    @ApiModelProperty("股票代码")
    private String code;
    @ApiModelProperty("股票名称")
    private String name;
    // 当前价
    @ApiModelProperty("最新价")
    private BigDecimal price;
    // 昨天收盘价
    @ApiModelProperty("昨日收盘价")
    private BigDecimal closePrice;
    // 开盘价
    @ApiModelProperty("今日开盘价")
    private BigDecimal openPrice;
    // 最高级
    @ApiModelProperty("今日最高价")
    private BigDecimal highPrice;
    // 最低价
    @ApiModelProperty("今日最低价")
    private BigDecimal lowPrice;
    //成交额
    @ApiModelProperty("成交额")
    private BigDecimal volumnPrice;
    // 成交量
    @ApiModelProperty("成交量")
    private BigDecimal volumn;
    // 振幅
    @ApiModelProperty("振幅")
    private BigDecimal swing;
    // 转手率
    @ApiModelProperty("换手率")
    private BigDecimal turnoverRate;
    // 日期
    @JSONField(format = "yyyy-MM-dd")
    @ApiModelProperty("日期")
    private Date lastDate;
    // 时间
    @ApiModelProperty("时间")
    private String lastTime;
    // 是否停牌
    @ApiModelProperty("是否停牌")
    private Boolean isStop;
    // 股票类型： 0=>股票；1->指数
    @ApiModelProperty("股票类型: 0->股票；1->指数")
    private Integer type;
    // 市盈率
    @ApiModelProperty("市盈率")
    private BigDecimal peRatio;
    // 市净率
    @ApiModelProperty("市净率")
    private BigDecimal cityNetRate;
    // 流通市值
    @ApiModelProperty("流通市值")
    private BigDecimal circulationValue;
    // 总市值
    @ApiModelProperty("总市值")
    private BigDecimal totalValue;

    @ApiModelProperty("买一价")
    private BigDecimal buy_1;
    private BigDecimal buy_2;
    private BigDecimal buy_3;
    private BigDecimal buy_4;
    private BigDecimal buy_5;

    @ApiModelProperty("买一委托量")
    private BigDecimal buy_1_s;
    private BigDecimal buy_2_s;
    private BigDecimal buy_3_s;
    private BigDecimal buy_4_s;
    private BigDecimal buy_5_s;

    @ApiModelProperty("卖一价")
    private BigDecimal sell_1;
    private BigDecimal sell_2;
    private BigDecimal sell_3;
    private BigDecimal sell_4;
    private BigDecimal sell_5;

    // 卖1委托量
    @ApiModelProperty("卖一委托量")
    private BigDecimal sell_1_s;
    // 卖2委托量
    private BigDecimal sell_2_s;
    // 卖3委托量
    private BigDecimal sell_3_s;
    // 卖4委托量
    private BigDecimal sell_4_s;
    // 卖5委托量
    private BigDecimal sell_5_s;


}
