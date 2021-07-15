package com.zhiyun.stock.models.repository;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: Kline
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 13:47
 **/
@Data
@Document
@ApiModel("k线")
public class Kline implements Serializable {
    @Id
    @JsonIgnore
    private String id;

    @Indexed
    @ApiModelProperty("股票代码")
    private String code;
    @ApiModelProperty("股票名称")
    private String name;
    // 最高价
    @ApiModelProperty("股票最高价")
    private BigDecimal high;
    // 最低价
    @ApiModelProperty("股票最低价")
    private BigDecimal low;
    // 收盘价
    @ApiModelProperty("收盘价")
    private BigDecimal close;
    // 开盘价
    @ApiModelProperty("开盘价")
    private BigDecimal open;
    // 交易量
    @ApiModelProperty("交易量")
    private BigDecimal volumn;
    // 交易额
    @ApiModelProperty("交易额")
    private BigDecimal volumnPrice;

    @ApiModelProperty("日期")
    @JSONField(format = "yyyyMMdd")
    @JsonFormat(pattern = "yyyyMMdd")
    private Date date;
}
