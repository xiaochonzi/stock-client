package com.zhiyun.stock.schedule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.enums.MongoDB;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.models.repository.Kline;
import com.zhiyun.stock.mongo.MongoFactory;
import com.zhiyun.stock.properties.AppInfoProperties;
import com.zhiyun.stock.properties.ApplicationProperties;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.utils.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @program: SynKlineTask
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 13:32
 **/
@Slf4j
@Component
public class SynKlineTask {

    private String klineServer;

    public SynKlineTask(ApplicationProperties properties) {
        while (klineServer == null) {
            klineServer = Common.getKlineServer(properties.getServerHost());
        }
    }

    @Autowired
    private MongoFactory mongoFactory;

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private Executor executor;

    @Scheduled(cron = "0 30 15 * * 1-6")
    public void synKline() {
        Set<String> rows = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        for (String row : rows) {
            String[] rowArr = row.split(":");
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    synOneStockKline(rowArr[0], MongoDB.day_data);
                    synOneStockKline(rowArr[0], MongoDB.day_before);
                    synOneStockKline(rowArr[0], MongoDB.day_after);
                }
            });
        }
    }

    public void synOneStockKline(String code, MongoDB db) {
        String collection_name = new StringBuilder(code).append("_").append(db.getCycle()).append("_").append(db.getFq()).toString();
        Query query = new Query();
        query.with(Sort.by(Sort.Order.desc("date")));
        Kline kline = mongoFactory.select(db).findOne(query, Kline.class, collection_name);
        String data = null;
        if (kline == null) {
            data = getKlineData(code, db.getCycle(), db.getFq(), 1000, 1);
        } else {
            int days = DateUtil.compare(kline.getDate(), new Date()) + 1;
            data = getKlineData(code, db.getCycle(), db.getFq(), days, 1);
        }
        if (StrUtil.isNotBlank(data)) {
            JSONArray array = JSON.parseArray(data);
            for (int i = 0; i < array.size(); i++) {
                String[] arr = array.getString(i).split(",");
                if (arr.length < 7) {
                    continue;
                }
                JSONObject object = new JSONObject();
                object.put("date", arr[0]);
                object.put("open", arr[1]);
                object.put("high", arr[2]);
                object.put("low", arr[3]);
                object.put("close", arr[4]);
                object.put("volumn", arr[5]);
                object.put("volumnPrice", arr[6]);
                Kline model = object.toJavaObject(Kline.class);
                String id = arr[0] + code;
                model.setId(id);
                mongoFactory.select(db).save(model, collection_name);
            }
        }
    }

    public String getKlineData(String code, String cycle, String fq, int pageSize, int isZip) {
        long t = System.currentTimeMillis();
        AppInfoProperties properties = AppInfoProperties.getInstance();
        String params = "code=" + code + "&cycle=" + cycle + "&fq=" + fq + "&page=1&pageSize=" + pageSize + "&zip=" + isZip;
        String token = Common.MD5(params + t + properties.getAppKey() + properties.getAppSecret());
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(klineServer).path("kline.php").query(params);
        builder.queryParam("t", t);
        builder.queryParam("app_key", properties.getAppKey());
        builder.queryParam("token", token);
        log.info("[getKlineData]{}", builder.toUriString());
        return HttpUtil.get(builder.toUriString());
    }
}
