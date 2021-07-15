package com.zhiyun.stock.messagehandle;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.dispatcher.MessageHandler;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: JsonNameHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 11:54
 **/
@Component
public class JsonNameHandler implements MessageHandler {

    @Autowired
    private Container container;

    @Override
    public void handle(Channel channel, String message) {
        if (StrUtil.startWith(message, "[")) {
            JSONArray array = JSON.parseArray(message);
            container.setFields(array);
        }
    }

    @Override
    public String getProtocol() {
        return Protocol.protocol_json_names;
    }
}
