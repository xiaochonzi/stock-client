package com.zhiyun.stock.runnables;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.enums.Dict;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.models.*;
import com.zhiyun.stock.properties.AppInfoProperties;
import com.zhiyun.stock.properties.ApplicationProperties;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.redis.RedisService;
import com.zhiyun.stock.utils.Common;
import com.zhiyun.stock.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @program: InitAllStockDataRunnable
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 14:23
 **/
@Slf4j
public class InitAllStockDataRunnable implements Runnable {

    private String klineServer;

    private RedisFactory redisFactory;

    public InitAllStockDataRunnable(ApplicationProperties applicationProperties, RedisFactory redisFactory) {
        this.redisFactory = redisFactory;
        log.info("InitAllStockDataRunnable");
        while (klineServer == null) {
            klineServer = Common.getKlineServer(applicationProperties.getServerHost());
        }
    }

    @Override
    public void run() {

        // 初始化板块
         initStockTypes();
        // 初始化好股票
        initStocks();
        // 初始化所有搜索库股票
        initSearchStocks();
        // 初始化所有股票实时行情
        initStockRealtimeQuotes();
        // 初始化财务数据
        initStockFinance();
        // 初始化除权数据
        initStockExrights();
    }

    private void initStockExrights() {
        log.info("[initStockExrights]开始初始化");
        String response = getDataFromServer("exrights.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                for (String key : dataJsonObject.keySet()) {
                    String value = dataJsonObject.getString(key);
                    List<StockExRightModel> list = new ArrayList<>();
                    try {
                        if (value.startsWith("{")) {
                            JSONObject valueJsonObject = JSONObject.parseObject(value);
                            valueJsonObject = JsonUtils.fixNull(valueJsonObject);
                            StockExRightModel model = valueJsonObject.toJavaObject(StockExRightModel.class);
                            list.add(model);
                        } else if (value.startsWith("[")) {
                            JSONArray valueJsonArr = JSONObject.parseArray(value);
                            for (int i = 0; i < valueJsonArr.size(); i++) {
                                JSONObject valueJsonObject = valueJsonArr.getJSONObject(i);
                                valueJsonObject = JsonUtils.fixNull(valueJsonObject);
                                StockExRightModel model = valueJsonObject.toJavaObject(StockExRightModel.class);
                                list.add(model);
                            }
                        }
                        if (list.size() > 0) {
                            redisFactory.select(RedisDB.MARKET).hSet(Dict.ExRight.name(), key, list);
                        }
                    } catch (Exception e) {
                        log.error("出错了{}", value, e);
                        break;
                    }

                }
            } catch (Exception e) {
                log.error("[initStockExrights]出错", e);
            }
        }
        log.info("[initStockExrights]初始化结束");
    }

    private void initStockFinance() {
        log.info("[initStockFinance]开始初始化");

        String response = getDataFromServer("finance.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                if (dataJsonObject != null) {
                    for (String key : dataJsonObject.keySet()) {
                        String value = dataJsonObject.getString(key);
                        try {
                            JSONObject valueJsonObject = JSONObject.parseObject(value);
                            valueJsonObject = JsonUtils.fixNull(valueJsonObject);
                            StockFinanceModel model = valueJsonObject.toJavaObject(StockFinanceModel.class);
                            redisFactory.select(RedisDB.MARKET).hSet(Dict.Financial.name(), key, model);
                        } catch (Exception e) {
                            log.error("[initStockFinance]出错了:{}", value, e);
                            break;
                        }

                    }
                }
            } catch (Exception e) {
                log.error("[initStockFinance]出错", e);
            }
        }

        log.info("[initStockFinance]初始化结束");
    }

    private void initStockRealtimeQuotes() {
        log.info("[initStockRealtimeQuotes]开始初始化");
        String response = getDataFromServer("allstockquotes.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                if (dataJsonObject != null) {
                    String names = dataJsonObject.getString("names");
                    names = names.replace("[", "");
                    names = names.replace("]", "");
                    JSONArray namekeys = JSON.parseArray("[" + names + "]");
                    JSONArray dataArr = dataJsonObject.getJSONArray("datas");
                    for (int i = 0; i < dataArr.size(); i++) {
                        String rows = dataArr.getString(i);
                        JSONObject object = JsonUtils.strToJsonObject(rows, namekeys);
                        try {
                            object = JsonUtils.fixNull(object);
                            StockRealTimeModel model = object.toJavaObject(StockRealTimeModel.class);
                            redisFactory.select(RedisDB.STOCK_REAL_TIME).set(model.getCode(), model);
                        } catch (Exception e) {
                            log.error("[出错]{}", object.toJSONString(), e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[initStockRealtimeQuotes]出错", e);
            }
        }
        log.info("[initStockRealtimeQuotes]初始化结束");
    }

    private void initSearchStocks() {
        log.info("[initSearchStocks]开始初始化");
        Set<String> keys = redisFactory.select(RedisDB.STOCK_SEARCH).keys("*");
        if (keys!=null && keys.size()>0){
            return;
        }
        String response = getDataFromServer("search.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONArray dataJsonArr = jsonObject.getJSONArray("data");
                if (dataJsonArr != null && dataJsonArr.size() > 0) {
                    for (int i = 0; i < dataJsonArr.size(); i++) {
                        String rows = dataJsonArr.getString(i);
                        List<String> rowList = StrUtil.splitTrim(rows, "|");
                        try {
                            StockSearchModel model = new StockSearchModel();
                            model.setCode(rowList.get(0));
                            model.setNamePrefix(rowList.get(1));
                            model.setName(rowList.get(2));
                            model.setType(Integer.parseInt(rowList.get(3)));
                            redisFactory.select(RedisDB.STOCK_SEARCH).set(rows, model);
                        } catch (Exception e) {
                            log.error("解析出错{}", rows, e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[initSearchStocks]出错", e);
            }
        }
        log.info("[initSearchStocks]初始化结束");
    }

    private void initStocks() {
        log.info("[initStocks]开始初始化");
        Set<String> keys = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        if (keys!=null && keys.size()>0){
            return;
        }
        String response = getDataFromServer("stocks.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONArray dataJsonObject = jsonObject.getJSONArray("data");
                if (dataJsonObject != null && dataJsonObject.size() > 0) {
                    for (int i = 0; i < dataJsonObject.size(); i++) {
                        String rows = dataJsonObject.getString(i);
                        String key = rows;
                        if (rows.indexOf(":tree:") > 0) {
                            String[] a = rows.split(":tree:");
                            if (a.length > 0) {
                                String code = a[0];
                                rows = a[1];
                                if (rows.indexOf("typeIds:") > 0) {
                                    a = rows.split("typeIds:");
                                    if (a.length > 0) {
                                        String tree = a[0];
                                        String typeIds = a[1];
                                        int type = 0;
                                        if (code.startsWith("sh000") || code.startsWith("sz399")) {
                                            type = 1;
                                        }
                                        // sh600169:tree:0,666666 typeIds:,666666,6,67,130,192,201,2...	;
                                        StockTreeModel model = new StockTreeModel();
                                        model.setStockCode(code);
                                        List<String> typeTree = StrUtil.splitTrim(tree, ",");
                                        model.setTypeTree(typeTree);
                                        List<String> typeIdsList = StrUtil.splitTrim(typeIds, ",");
                                        model.setTypeIds(typeIdsList);
                                        model.setStopFlag(false);
                                        model.setType(type);
                                        redisFactory.select(RedisDB.STOCK_TREE_DATA).set(key, model);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[initStocks]出错", e);
            }
        }
        log.info("[initStocks]初始化结束");
    }

    private void initStockTypes() {
        log.info("[initStockTypes]开始初始化");
        RedisService redisService = redisFactory.select(RedisDB.MARKET);
        if (redisService.hasKey(Dict.HangYe.name())
        && redisService.hasKey(Dict.GaiNian.name())
        && redisService.hasKey(Dict.DiQu.name())){
            return;
        }
        String response = getDataFromServer("stocktypes.php");
        if (StrUtil.isNotBlank(response)) {
            try {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject dataJsonObject = jsonObject.getJSONObject("data");
                JSONArray hangyeJsonArr = dataJsonObject.getJSONArray("hangye");
                if (hangyeJsonArr != null && hangyeJsonArr.size() > 0) {
                    List<CategoryModel> categoryModels = hangyeJsonArr.toJavaList(CategoryModel.class);
                    redisFactory.select(RedisDB.MARKET).set(Dict.HangYe.name(), categoryModels);
                }
                JSONArray gainianJsonArr = dataJsonObject.getJSONArray("gainian");
                if (gainianJsonArr != null && gainianJsonArr.size() > 0) {
                    List<CategoryModel> categoryModels = gainianJsonArr.toJavaList(CategoryModel.class);
                    redisFactory.select(RedisDB.MARKET).set(Dict.GaiNian.name(), categoryModels);
                }
                JSONArray diquJsonArr = dataJsonObject.getJSONArray("diqu");
                if (diquJsonArr != null && diquJsonArr.size() > 0) {
                    List<CategoryModel> categoryModels = diquJsonArr.toJavaList(CategoryModel.class);
                    redisFactory.select(RedisDB.MARKET).set(Dict.DiQu.name(), categoryModels);
                }
            } catch (Exception e) {
                log.error("[initStockTypes]出错", e);
            }

        }
        log.info("[initStockTypes]结束初始化");
    }

    private String getDataFromServer(String path) {
        long t = System.currentTimeMillis();
        AppInfoProperties properties = AppInfoProperties.getInstance();
        String token = Common.MD5("" + t + properties.getAppKey() + properties.getAppSecret());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(klineServer).path(path);
        builder.queryParam("t", t);
        builder.queryParam("app_key", properties.getAppKey());
        builder.queryParam("token", token);
        log.info("[getDataFromServer]{}", builder.toUriString());
        return HttpUtil.get(builder.toUriString());
    }


}
