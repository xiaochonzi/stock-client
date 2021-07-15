package com.zhiyun.stock.mongo;

import com.zhiyun.stock.enums.MongoDB;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: MongoFactory
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 13:53
 **/
public class MongoFactory {
    private Map<MongoDB, MongoTemplate> mongoDBMongoTemplateMap = new ConcurrentHashMap<>();

    public void setMongoDBMongoTemplateMap(MongoDB db, MongoTemplate mongoTemplate) {
        mongoDBMongoTemplateMap.put(db, mongoTemplate);
    }

    public MongoTemplate select(MongoDB mongoDB) {
        return mongoDBMongoTemplateMap.get(mongoDB);
    }
}
