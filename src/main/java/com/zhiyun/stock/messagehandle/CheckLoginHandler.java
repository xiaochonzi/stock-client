package com.zhiyun.stock.messagehandle;

import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.dispatcher.MessageHandler;
import com.zhiyun.stock.properties.ApplicationProperties;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.runnables.InitAllStockDataRunnable;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

/**
 * @program: CheckLoginHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 11:54
 **/
@Component
public class CheckLoginHandler implements MessageHandler {

    @Autowired
    private ApplicationProperties properties;

    @Autowired
    private Executor executor;

    @Autowired
    private RedisFactory redisFactory;

    @Override
    public void handle(Channel channel, String message) {
         executor.execute(new InitAllStockDataRunnable(properties, redisFactory));
    }

    @Override
    public String getProtocol() {
        return Protocol.protocol_checklogin;
    }
}
