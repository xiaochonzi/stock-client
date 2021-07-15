package com.zhiyun.stock.enums;

public enum RedisDB {
    // 市场信息
    MARKET(3),
    // 股票实时行情
    STOCK_REAL_TIME(4),
    // K线数据
    KLINE_DATA(5),
    // 股票tree数据
    STOCK_TREE_DATA(6),
    // 股票分钟数据
    MINUTE_KLINE_DATA(7),
    // 股票搜索
    STOCK_SEARCH(10),
    // 分时信息
    STOCK_TIMELINE(11),
    ;
    int index;

    RedisDB(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
