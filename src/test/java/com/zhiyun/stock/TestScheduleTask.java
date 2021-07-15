package com.zhiyun.stock;

import com.zhiyun.stock.enums.MongoDB;
import com.zhiyun.stock.schedule.MarketUpDownTask;
import com.zhiyun.stock.schedule.SynKlineTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: TestScheduleTask
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/9 14:02
 **/
public class TestScheduleTask extends StockClientApplicationTests{

    @Autowired
    private MarketUpDownTask task;

    @Autowired
    private SynKlineTask synKlineTask;

    @Test
    public void testStockChangeRate(){
        task.stockChangeRateUnDown();
    }

    @Test
    public void testSynKlineTask(){
        synKlineTask.synKline();
    }

    @Test
    public void testUnDown(){
        task.GaiNianUnDown();
    }

    @Test
    public void testSynKline(){
        synKlineTask.getKlineData("sz000001", "day", "data", 100, 2);
    }
}
