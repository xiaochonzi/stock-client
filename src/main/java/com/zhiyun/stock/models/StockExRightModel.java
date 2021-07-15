package com.zhiyun.stock.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: StockExRightModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/6 10:26
 **/
@Data
public class StockExRightModel {
    private String code;
    private String name;

    @JSONField(format = "yyyy-MM-dd")
    private Date datetime;

    // 每股送
    private BigDecimal give;
    // 每股配
    private BigDecimal pei;
    // 配股价
    private BigDecimal perPrice;
    // 每股红利
    private BigDecimal profile;
}
