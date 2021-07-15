package com.zhiyun.stock.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.redis.RedisService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @program: RedisConfigure
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 17:09
 **/
@Configuration
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfigure {

    @Bean
    public RedisFactory redisService(RedisProperties redisProperties) {
        RedisFactory redisFactory = new RedisFactory();
        RedisDB[] dbs = RedisDB.values();
        for (RedisDB db : dbs) {
            redisProperties.setDatabase(db.getIndex());
            RedisConnectionFactory connectionFactory = redisConnectionFactory(redisProperties);
            RedisTemplate redisTemplate = createRedisTemplate(connectionFactory);
            RedisService redisService = new RedisService(redisTemplate);
            redisFactory.setRedisService(db, redisService);
        }
        return redisFactory;
    }

    private RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(serializer);
        template.setDefaultSerializer(serializer);
        template.setConnectionFactory(redisConnectionFactory);
        template.afterPropertiesSet();
        return template;
    }


    private RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        JedisPoolConfig poolConfig = jedisPoolConfig(redisProperties);
        jedisConnectionFactory.setPoolConfig(poolConfig);
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
        return jedisConnectionFactory;
    }

    private JedisPoolConfig jedisPoolConfig(RedisProperties properties) {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(properties.getJedis().getPool().getMaxActive());
        config.setMaxIdle(properties.getJedis().getPool().getMaxIdle());
        config.setMaxWaitMillis(properties.getJedis().getPool().getMaxWait().toMillis());
        return config;
    }
}
