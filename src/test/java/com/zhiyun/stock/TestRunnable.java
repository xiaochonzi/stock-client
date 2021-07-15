package com.zhiyun.stock;

import com.zhiyun.stock.properties.ApplicationProperties;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.runnables.InitAllStockDataRunnable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: TestRunnable
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 22:53
 **/
public class TestRunnable extends StockClientApplicationTests{

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private RedisFactory redisFactory;

    @Test
    public void initStockData(){
        InitAllStockDataRunnable runnable = new InitAllStockDataRunnable(applicationProperties, redisFactory);
        runnable.run();
    }
}
