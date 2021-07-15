package com.zhiyun.stock.messagehandle;

import com.alibaba.fastjson.JSONArray;
import com.zhiyun.stock.models.StockRealTimeModel;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: Container
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/6 17:21
 **/
@Data
@Component
public class Container {

    private static List<JSONArray> stockFields = new ArrayList<>();

    public static Map<String, StockRealTimeModel> realTimeModelMap = new ConcurrentHashMap<>(6000);

    public synchronized void setFields(JSONArray array) {
        stockFields.add(0, array);
    }

    public synchronized JSONArray getFields() {
        if (stockFields.size() > 0) {
            return stockFields.get(0);
        } else {
            return null;
        }
    }

    public void putStock(StockRealTimeModel model){
        realTimeModelMap.put(model.getCode(), model);
    }

    public StockRealTimeModel getStock(String code){
        return realTimeModelMap.get(code);
    }
}
