package com.zhiyun.stock.redis;

import com.zhiyun.stock.enums.RedisDB;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: RedisService
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 17:00
 **/
public class RedisFactory {

    private Map<RedisDB, RedisService> redisTemplateHashMap = new ConcurrentHashMap<>();

    public void setRedisService(RedisDB db, RedisService redisService) {
        redisTemplateHashMap.put(db, redisService);
    }

    public RedisService select(RedisDB db) {
        return redisTemplateHashMap.get(db);
    }

}
