package com.zhiyun.stock.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @program: ApplicationProperties
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 15:01
 **/
@Data
@ConfigurationProperties("stock")
public class ApplicationProperties {
    // 服务器地址
    private String serverHost;
    // 是否采集历史k线数据
    private Boolean collectHistoryKlineFlag;
    // 是否采集分钟K线数据
    private Boolean collectHistoryMinuteKlineFlag;
    // 是否采集分钟复权数据
    private Boolean collectHistoryMinuteKlineFqFlag;
}
