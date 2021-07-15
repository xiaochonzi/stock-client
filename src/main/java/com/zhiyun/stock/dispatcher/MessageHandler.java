package com.zhiyun.stock.dispatcher;

import io.netty.channel.Channel;

/**
 * @program: MessageHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/4 22:20
 **/
public interface MessageHandler {

    void handle(Channel channel, String message);

    String getProtocol();
}
