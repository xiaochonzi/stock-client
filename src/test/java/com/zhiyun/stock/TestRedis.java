package com.zhiyun.stock;

import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.models.StockRealTimeModel;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.redis.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: TestRedis
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 17:44
 **/
public class TestRedis extends StockClientApplicationTests{

    @Autowired
    private RedisFactory redisFactory;

    @Test
    public void insertDB(){
        redisFactory.select(RedisDB.KLINE_DATA).set("test", "ddd");
        assert redisFactory.select(RedisDB.MARKET).hasKey("test");
    }

    @Test
    public void getStockRealTime(){
        StockRealTimeModel model = redisFactory.select(RedisDB.STOCK_REAL_TIME).get("sh000001");
        System.out.println(model.getName());
    }
}
