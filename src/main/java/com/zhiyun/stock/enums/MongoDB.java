package com.zhiyun.stock.enums;

/**
 * @program: MongoDB
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 13:53
 **/
public enum MongoDB {
    day_data("day", "data"),
    day_before("day", "before"),
    day_after("day", "after"),
    ;

    private String cycle;
    private String fq;

    MongoDB(String cycle, String fq){
        this.cycle = cycle;
        this.fq = fq;
    }

    public String getCycle() {
        return cycle;
    }

    public String getFq() {
        return fq;
    }
}
