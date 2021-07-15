package com.zhiyun.stock.messagehandle;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.dispatcher.MessageHandler;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.models.StockKlineModel;
import com.zhiyun.stock.models.StockRealTimeModel;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.redis.RedisService;
import com.zhiyun.stock.utils.JsonUtils;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: RealTimeHandler
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 11:55
 **/
@Slf4j
@Component
public class RealTimeHandler implements MessageHandler {
    @Autowired
    private Container container;

    @Autowired
    private RedisFactory redisFactory;

    @Override
    public void handle(Channel channel, String message) {
        JSONArray fields = container.getFields();
        RedisTemplate realTimeDB = redisFactory.select(RedisDB.STOCK_REAL_TIME).getRedisTemplate();
        if (StrUtil.isNotBlank(message)) {
            JSONObject obj = JSON.parseObject(message);
            realTimeDB.executePipelined(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.openPipeline();
                    for (String key : obj.keySet()) {
                        String value = obj.getString(key);
                        JSONObject object = JsonUtils.strToJsonObject(value, fields);
                        object = JsonUtils.fixNull(object);
                        StockRealTimeModel model = object.toJavaObject(StockRealTimeModel.class);
                        container.putStock(model);
                        redisConnection.set(realTimeDB.getKeySerializer().serialize(key), realTimeDB.getValueSerializer().serialize(model));
                    }
                    redisConnection.closePipeline();
                    log.info("[实时行情]当前时时间:{}接受{}笔", DateUtil.format(new Date(), "HH:mm:ss"), obj.keySet().size());
                    return null;
                }
            });
            RedisService timeLineRedis = redisFactory.select(RedisDB.STOCK_TIMELINE);
            for (String key : obj.keySet()) {
                String value = obj.getString(key);
                JSONObject object = JsonUtils.strToJsonObject(value, fields);
                object = JsonUtils.fixNull(object);
                StockRealTimeModel lastModel = object.toJavaObject(StockRealTimeModel.class);
                String timeLineKey = lastModel.getCode() + "_today";
                String timeKey = StrUtil.replace(lastModel.getLastTime(), ":", "").substring(0, 4);
                StockKlineModel klineModel = timeLineRedis.hGet(timeLineKey, timeKey);
                if (klineModel == null) {
                    klineModel = new StockKlineModel();
                    klineModel.setCode(lastModel.getCode());
                    klineModel.setName(lastModel.getName());
                    klineModel.setOpen(lastModel.getPrice());
                    klineModel.setClose(lastModel.getPrice());
                    klineModel.setHigh(lastModel.getPrice());
                    klineModel.setLow(lastModel.getPrice());
                    klineModel.setVolumnPrice(lastModel.getVolumnPrice());
                    klineModel.setVolumn(lastModel.getVolumn());
                    klineModel.setStartVolumn(lastModel.getVolumn());
                    klineModel.setDate(lastModel.getLastDate());
                    klineModel.setTime(lastModel.getLastTime());
                } else {
                    BigDecimal price = lastModel.getPrice();
                    if (klineModel.getHigh().compareTo(price) < 0) {
                        klineModel.setHigh(price);
                    }
                    if (klineModel.getLow().compareTo(price) > 0) {
                        klineModel.setLow(price);
                    }
                    klineModel.setClose(price);
                }
                timeLineRedis.hSet(timeLineKey, timeKey, klineModel);
            }
        }
    }

    @Override
    public String getProtocol() {
        return Protocol.protocol_realtime;
    }
}
