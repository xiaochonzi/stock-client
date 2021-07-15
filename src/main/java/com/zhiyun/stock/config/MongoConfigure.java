package com.zhiyun.stock.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.zhiyun.stock.enums.MongoDB;
import com.zhiyun.stock.mongo.MongoFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: MongoConfigure
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 15:28
 **/
@Configuration
public class MongoConfigure {

    @Autowired
    private Environment environment;

    @Autowired
    ObjectProvider<MongoClientSettingsBuilderCustomizer> builderCustomizers;
    @Autowired
    ObjectProvider<MongoClientSettings> settings;

    @Bean
    public MongoFactory mongoFactory(MongoProperties properties) {
        MongoFactory mongoFactory = new MongoFactory();
        for (MongoDB db : MongoDB.values()) {
            properties.setDatabase(db.name());
            MongoTemplate mongoTemplate = createMongoTemplate(properties);
            mongoFactory.setMongoDBMongoTemplateMap(db, mongoTemplate);
        }
        return mongoFactory;
    }

    public MongoTemplate createMongoTemplate(MongoProperties properties) {
        MongoClient client = (MongoClient) (new MongoClientFactory(properties, environment, (List) builderCustomizers.orderedStream().collect(Collectors.toList()))).createMongoClient((MongoClientSettings) settings.getIfAvailable());
        MongoDatabaseFactorySupport mongoDatabaseFactory = new SimpleMongoClientDatabaseFactory(client, properties.getMongoClientDatabase());
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDatabaseFactory);
        return mongoTemplate;
    }
}
