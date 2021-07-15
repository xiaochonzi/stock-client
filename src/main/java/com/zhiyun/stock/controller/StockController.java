package com.zhiyun.stock.controller;

import com.zhiyun.stock.enums.Dict;
import com.zhiyun.stock.enums.MongoDB;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.models.CategoryChangeRateModel;
import com.zhiyun.stock.models.StockRealTimeModel;
import com.zhiyun.stock.models.StockUpDownModel;
import com.zhiyun.stock.models.repository.Kline;
import com.zhiyun.stock.mongo.MongoFactory;
import com.zhiyun.stock.mvc.Response;
import com.zhiyun.stock.redis.RedisFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @program: QuotesController
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/12 20:37
 **/
@Api(tags = "股票模块")
@RestController
public class StockController {

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private MongoFactory mongoFactory;

    @ApiOperation("搜索股票")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "text", value = "搜索内容(股票中文,代码，中文拼音简写)")
    })
    @GetMapping("search")
    public Response<Set<String>> search(String text) {
        Set<String> keys = redisFactory.select(RedisDB.STOCK_SEARCH).keys("*" + text + "*");
        return Response.ok(keys);
    }

    @ApiOperation("实时行情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codes", value = "代码, 多个代码用,分割")
    })
    @GetMapping("quotes")
    public Response<Map<String, StockRealTimeModel>> quotes(String[] codes) {
        Map<String, StockRealTimeModel> results = new HashMap<>();
        for (String code : codes) {
            StockRealTimeModel model = redisFactory.select(RedisDB.STOCK_REAL_TIME).get(code);
            results.put(code, model);
        }
        return Response.ok(results);
    }

    @ApiOperation("板块涨幅榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型: hangye->行业;diqu->地区;gainian->概念")
    })
    @GetMapping("plateranking")
    public Response<List<CategoryChangeRateModel>> plateranking(String type) {
        Dict dict = null;
        switch (type) {
            case "hangye":
                dict = Dict.HangYeUpDown;
                break;
            case "diqu":
                dict = Dict.DiQuUpDown;
                break;
            case "gainian":
                dict = Dict.GaiNianUpDown;
                break;

        }
        if (dict == null) {
            return Response.ok();
        }
        List<CategoryChangeRateModel> results = redisFactory.select(RedisDB.MARKET).lGetAll(dict.name());
        return Response.ok(results);
    }

    @ApiOperation("个股涨幅榜")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type",
                    value = "类型: changerate->涨跌榜;swing->振幅榜;turnover->换手率榜;totalValue->总市值榜;circulationValue->流通市值榜")
    })
    @GetMapping("updownlist")
    public Response<List<StockUpDownModel>> updownlist(String type){
        Dict dict = null;
        switch (type){
            case "changerate":
                dict = Dict.StockChangeRateUnDown;
                break;
            case "swing":
                dict = Dict.StockSwingUpDown;
                break;
            case "turnover":
                dict = Dict.StockTurnOverRateUpDown;
                break;
            case "totalValue":
                dict = Dict.StockTotalValueUpDown;
                break;
            case "circulationValue":
                dict = Dict.StockCirculationUpDown;
                break;
        }
        if (dict==null){
            return Response.ok();
        }
        List<StockUpDownModel> results = redisFactory.select(RedisDB.MARKET).lGetAll(dict.name());
        return Response.ok(results);
    }

    @ApiOperation("k线数据")
    @ApiImplicitParams({
         @ApiImplicitParam(name = "code", value = "股票代码"),
         @ApiImplicitParam(name = "cycle", value = "k线周期:day->日K"),
         @ApiImplicitParam(name = "fq", value = "复权: data->不复权;before->前复权;after->后复权"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小")
    })
    @GetMapping("kline")
    public Response<List<Kline>> kline(@RequestParam("code") String code,
                                       @RequestParam("cycle") String cycle,
                                       @RequestParam("fq") String fq,
                                       @RequestParam(value = "page",required = false, defaultValue = "1") Integer page,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "500") Integer pageSize){
        String key = new StringBuilder(cycle).append("_").append(fq).toString();
        MongoDB db = MongoDB.valueOf(key.toLowerCase());
        if (db==null){
            return Response.error("参数不正确");
        }
        String collection_name = new StringBuilder(code).append("_").append(db.getCycle()).append("_").append(db.getFq()).toString();
        if (page<=0){
            page = 1;
        }
        if (pageSize==null){
            pageSize = 500;
        }
        Pageable pageable = PageRequest.of(page, pageSize);
        Query query = new Query();
        query.with(pageable);
        List<Kline> results = mongoFactory.select(db).find(query, Kline.class, collection_name);
        return Response.ok(results);
    }
}
