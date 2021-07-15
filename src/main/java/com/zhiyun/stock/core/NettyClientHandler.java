package com.zhiyun.stock.core;

import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.utils.Common;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @program: NettyClientHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/4 13:29
 **/
@Slf4j
@Component
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private NettyClient nettyClient;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String message = Common.login();
        ctx.writeAndFlush(Unpooled.copiedBuffer(message.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 发起重连
        nettyClient.reconnect();
        // 继续触发事件
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("[exceptionCaught][连接({}) 发生异常]", ctx.channel().id(), cause);
        // 断开连接
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
        // 空闲时，向服务端发起一次心跳
        if (event instanceof IdleStateEvent) {
            log.info("[userEventTriggered][发起一次心跳]");
            String heartbeat = Common.dataForWrite(Protocol.protocol_heart, new JSONObject());
            ctx.writeAndFlush(Unpooled.copiedBuffer(heartbeat.getBytes(StandardCharsets.UTF_8)));
        } else {
            super.userEventTriggered(ctx, event);
        }
    }
}
