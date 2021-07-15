package com.zhiyun.stock.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: MessageHandlerAdapter
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/4 22:22
 **/
@Slf4j
public class MessageHandlerAdapter implements InitializingBean, ApplicationContextAware {

    // 协议对应的处理器
    private final Map<String, MessageHandler> handlers = new HashMap<>();

    ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        applicationContext.getBeansOfType(MessageHandler.class).values() // 获得所有 MessageHandler Bean
                .forEach(messageHandler -> handlers.put(messageHandler.getProtocol(), messageHandler)); // 添加到 handlers 中
        log.info("[afterPropertiesSet][消息处理器数量：{}]", handlers.size());
    }

    /**
     * 获得类型对应的 MessageHandler
     *
     * @param protocol 协议
     * @return MessageHandler
     */
    MessageHandler getMessageHandler(String protocol) {
        MessageHandler handler = handlers.get(protocol);
//        if (handler == null) {
//            throw new IllegalArgumentException(String.format("协议(%s) 找不到匹配的 MessageHandler 处理器", protocol));
//        }
        return handler;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
