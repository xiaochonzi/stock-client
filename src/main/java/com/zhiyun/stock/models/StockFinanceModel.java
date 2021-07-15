package com.zhiyun.stock.models;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: StockFinanceModel
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/6 09:33
 **/
@Data
public class StockFinanceModel {
    private String code;
    // 总股本
    private BigDecimal zgb;
    // 国家股
    private BigDecimal gjg;
    // 流通A股
    private BigDecimal ltag;
    // 总资产
    private BigDecimal zzc;
    // 流动资产
    private BigDecimal ldzc;
    // 固定资产
    private BigDecimal gdzc;
    // 净资产
    private BigDecimal jzc;
    // 营业收入
    private BigDecimal zysy;
    // 主营利润
    private BigDecimal zyly;
    // 营业利润
    private BigDecimal yyly;
    // 税后利润
    private BigDecimal shly;
    // 净利润
    private BigDecimal jly;
    // 地域
    private Integer dy;
    // 行业
    private Integer hy;
    // 资料月份
    private Integer zbnb;
    // 上市时间
    @JSONField(format = "yyyyMMdd")
    private Date ssdate;
    // 日期
    @JSONField(format = "yyyyMMdd")
    private Date datetime;
}
