package com.zhiyun.stock.messagehandle;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.dispatcher.MessageHandler;
import com.zhiyun.stock.properties.AppInfoProperties;
import com.zhiyun.stock.utils.Common;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @program: ReigsterHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 12:03
 **/
@Slf4j
@Component
public class RegisterHandler implements MessageHandler {

    @Override
    public void handle(Channel channel, String message) {
        JSONObject jsonObject = JSON.parseObject(message);
        int error = jsonObject.getIntValue("error");
        if (error == 0) {
            JSONObject appInfo = jsonObject.getJSONObject("msg");
            String appKey = appInfo.getString("app_key");
            String appSecret = appInfo.getString("app_secret");
            String appId = appInfo.getString("app_id");
            String appPassword = appInfo.getString("app_password");
            AppInfoProperties properties = AppInfoProperties.getInstance();
            properties.setAppSecret(appSecret);
            properties.setAppId(appId);
            properties.setAppPassword(appPassword);
            properties.setAppKey(appKey);
            properties.save();
            // 注册成功后登录
            String sendMsg = Common.login();
            channel.writeAndFlush(Unpooled.copiedBuffer(sendMsg.getBytes(StandardCharsets.UTF_8)));
        }
    }

    @Override
    public String getProtocol() {
        return Protocol.protocol_register;
    }
}
