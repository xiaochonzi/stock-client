package com.zhiyun.stock;

import com.zhiyun.stock.enums.MongoDB;
import com.zhiyun.stock.models.repository.Kline;
import com.zhiyun.stock.mongo.MongoFactory;
import com.zhiyun.stock.schedule.SynKlineTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: TestMongo
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 15:53
 **/
public class TestMongo extends StockClientApplicationTests{

    @Autowired
    private MongoFactory mongoFactory;

    @Autowired
    private SynKlineTask task;

    @Test
    public void testKlineUrl(){
        task.getKlineData("sh000001", "day","data", 1, 1);
    }

    @Test
    public void testInsertDocument(){
        Kline model = new Kline();
        model.setCode("1013");
        model.setId("333");
        model.setClose(new BigDecimal(1000));
        model.setDate(new Date());
        mongoFactory.select(MongoDB.day_data).save(model, "code_day_data");
    }

    @Test
    public void testQuery(){
        Query query = new Query(Criteria.where("code").is("1012"));
        Kline kline = mongoFactory.select(MongoDB.day_data).findOne(query, Kline.class, "code_day_data");
        assert kline!=null;
    }
}
