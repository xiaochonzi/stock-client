package com.zhiyun.stock.config;

import com.zhiyun.stock.dispatcher.MessageDispatcher;
import com.zhiyun.stock.dispatcher.MessageHandlerAdapter;
import com.zhiyun.stock.properties.ApplicationProperties;
import com.zhiyun.stock.properties.ServerProperties;
import com.zhiyun.stock.utils.Common;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: ClientConfigure
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/4 22:26
 **/
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class ClientConfigure {

    @Bean
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }

    @Bean
    public MessageHandlerAdapter messageHandlerAdapter() {
        return new MessageHandlerAdapter();
    }

    @Bean
    public ServerProperties serverProperties(ApplicationProperties properties) {
        return Common.getServerIp(properties.getServerHost());
    }


}
