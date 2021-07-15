package com.zhiyun.stock.dispatcher;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.utils.ZipUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URLDecoder;
import java.util.concurrent.Executor;

/**
 * @program: MessageDispatcher
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/4 22:19
 **/
@Slf4j
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<String> {

    @Autowired
    private Executor executor;

    @Autowired
    private MessageHandlerAdapter handlerAdapter;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        String unzipStr = ZipUtils.unzip(s);
        JSONObject jsonObject = JSON.parseObject(unzipStr);
        String protocol = jsonObject.getString("protocol");
        String data = jsonObject.getString("data");
        String messageStr = URLDecoder.decode(data, "UTF-8");
        //log.info("协议:[{}]", protocol);
        MessageHandler handler = handlerAdapter.getMessageHandler(protocol);
        if (handler != null) {
            handler.handle(channelHandlerContext.channel(), messageStr);
        } else {
            log.error("找不到处理器");
        }
    }
}
