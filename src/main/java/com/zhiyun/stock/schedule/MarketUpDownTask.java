package com.zhiyun.stock.schedule;

import com.zhiyun.stock.enums.Dict;
import com.zhiyun.stock.enums.RedisDB;
import com.zhiyun.stock.messagehandle.Container;
import com.zhiyun.stock.models.*;
import com.zhiyun.stock.redis.RedisFactory;
import com.zhiyun.stock.utils.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @program: MarketUpDown
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/8 11:12
 **/
@Slf4j
@Component
public class MarketUpDownTask {

    @Autowired
    private RedisFactory redisFactory;

    @Autowired
    private Container container;

    /**
     * 涨跌榜
     * 周一至周五 9点-15点，每2分钟计算一次
     */
    @Scheduled(cron = "0 0/2 9-15 * * 1-5")
    public void stockChangeRateUnDown() {
        log.info("[stockChangeRateUnDown]开始");
        Set<String> stockNames = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        calmStockUpDown(stockNames, Dict.StockChangeRateUnDown);
        log.info("[stockChangeRateUnDown]结束");
    }

    /**
     * 换手率
     * 周一至周五 9点-15点，每2分钟计算一次
     */
    @Scheduled(cron = "0 0/2 9-15 * * 1-5")
    public void stockTurnOverRateUpDown() {
        log.info("[stockTurnOverRateUpDown]开始");
        Set<String> stockNames = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        calmStockUpDown(stockNames, Dict.StockTurnOverRateUpDown);
        log.info("[stockTurnOverRateUpDown]结束");
    }

    /**
     * 振幅榜
     * 周一至周五 9点-15点，每2分钟计算一次
     */
    @Scheduled(cron = "0 0/2 9-15 * * 1-5")
    public void StockSwingUpDown() {
        log.info("[StockSwingUpDown]开始");
        Set<String> stockNames = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        calmStockUpDown(stockNames, Dict.StockSwingUpDown);
        log.info("[StockSwingUpDown]结束");
    }

    /**
     * 流通市值榜
     * 周一至周五 9点-15点，每3分钟计算一次
     */
    @Scheduled(cron = "0 0/10 9-15 * * 1-5")
    public void StockCirculationUpDown() {
        log.info("[StockCirculationUpDown]开始");
        Set<String> stockNames = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        calmStockUpDown(stockNames, Dict.StockCirculationUpDown);
        log.info("[StockCirculationUpDown]结束");
    }

    /**
     * 总市值榜
     * 周一至周五 9点-15点，每3分钟计算一次
     */
    @Scheduled(cron = "0 0/10 9-15 * * 1-5")
    public void StockTotalValueUpDown() {
        log.info("[StockTotalValueUpDown]开始");
        Set<String> stockNames = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*");
        calmStockUpDown(stockNames, Dict.StockTotalValueUpDown);
        log.info("[StockTotalValueUpDown]结束");
    }


    /**
     * 概念涨幅榜
     */
    @Scheduled(cron = "0 0/5 9-15 * * 1-5")
    public void GaiNianUnDown() {
        log.info("[GaiNianUnDown]开始");
        createCategoryUpDown(Dict.GaiNian);
        log.info("[GaiNianUnDown]结束");
    }

    /**
     * 行业涨幅榜
     */
    @Scheduled(cron = "0 0/5 9-15 * * 1-5")
    public void hangYeUpDown() {
        log.info("[hangYeUpDown]开始");
        createCategoryUpDown(Dict.HangYe);
        log.info("[hangYeUpDown]结束");
    }

    /**
     * 地区涨幅榜
     */
    @Scheduled(cron = "0 0/5 9-15 * * 1-5")
    public void DiQuUpDown() {
        log.info("[DiQuUpDown]开始");
        createCategoryUpDown(Dict.DiQu);
        log.info("[DiQuUpDown]结束");
    }


    private void calmStockUpDown(Set<String> stockNames, Dict dict) {
        List<StockUpDownModel> changeRateList = new ArrayList<>();
        for (String stockName : stockNames) {
            String[] lines = stockName.split(":");
            String code = lines[0];
            String codeNumber = code.substring(2);
            try {
                StockRealTimeModel realTimeModel = container.getStock(code);
                if (realTimeModel != null) {
                    if (0 == realTimeModel.getType()
                            && !realTimeModel.getIsStop()
                            && realTimeModel.getPrice() != null
                            && realTimeModel.getClosePrice() != null
                            && realTimeModel.getOpenPrice() != null
                            && !codeNumber.startsWith("90")
                            && !codeNumber.startsWith("20")
                            && (code.startsWith("sh") || code.startsWith("sz"))
                    ) {
                        BigDecimal compareValue = null;
                        StockUpDownModel model = new StockUpDownModel();
                        model.setName(realTimeModel.getName());
                        model.setCode(realTimeModel.getCode());
                        try {
                            switch (dict) {
                                case StockChangeRateUnDown:
                                    compareValue = realTimeModel.getPrice().subtract(realTimeModel.getClosePrice()).divide(realTimeModel.getClosePrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                                    break;
                                case StockSwingUpDown:
                                    if (realTimeModel.getSwing()!=null){
                                        compareValue = realTimeModel.getSwing().setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                    break;
                                case StockTotalValueUpDown:
                                    if (realTimeModel.getTotalValue()!=null){
                                        compareValue = realTimeModel.getTotalValue().setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                    break;
                                case StockCirculationUpDown:
                                    if (realTimeModel.getCirculationValue()!=null){
                                        compareValue = realTimeModel.getCirculationValue().setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                    break;
                                case StockTurnOverRateUpDown:
                                    if (realTimeModel.getTurnoverRate()!=null){
                                        compareValue = realTimeModel.getTurnoverRate().setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                    break;
                                default:
                                    break;
                            }
                            if (compareValue != null) {
                                model.setChangeRate(compareValue);
                                changeRateList.add(model);
                            }
                        } catch (Exception e) {
                            log.error("[calmStockUpDown]出错了{}", code, e);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("[calmStockUpDown]出错了{}", code, e);
            }
        }
        if (changeRateList!=null && changeRateList.size()>0){
            Collections.sort(changeRateList);
            redisFactory.select(RedisDB.MARKET).del(dict.name());
            redisFactory.select(RedisDB.MARKET).lSet(dict.name(), changeRateList);
            changeRateList.clear();
            System.gc();
        }
    }

    private void createCategoryUpDown(Dict dict) {
        List<CategoryModel> categoryModels = redisFactory.select(RedisDB.MARKET).get(dict.name());
        List<CategoryChangeRateModel> list = new ArrayList<>();
        if (categoryModels==null || categoryModels.size()==0){
            return;
        }
        for (CategoryModel model : categoryModels) {
            try {
                CategoryChangeRateModel categoryChangeRateModel = calcCategoryAverage(model);
                StockRealTimeModel realTimeModel = container.getStock(categoryChangeRateModel.getCode());
                if (realTimeModel != null) {
                    BigDecimal changeRate = realTimeModel.getPrice().subtract(realTimeModel.getClosePrice()).divide(realTimeModel.getClosePrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                    categoryChangeRateModel.setChangeRate(changeRate);
                    categoryChangeRateModel.setPrice(realTimeModel.getPrice());
                    categoryChangeRateModel.setName(realTimeModel.getName());
                    list.add(categoryChangeRateModel);
                }
            } catch (Exception e) {
                log.error("[createCategoryUpDown]出错了", e);
            }
        }
        CollectionUtils.sort(list, new Comparator<CategoryChangeRateModel>() {
            @Override
            public int compare(CategoryChangeRateModel o1, CategoryChangeRateModel o2) {
                return o2.getRate().compareTo(o1.getRate());
            }
        });
        String key = "";
        switch (dict) {
            case DiQu:
                key = Dict.DiQuUpDown.name();
                break;
            case HangYe:
                key = Dict.HangYeUpDown.name();
                break;
            case GaiNian:
                key = Dict.GaiNianUpDown.name();
                break;
        }
        if (list!=null && list.size()>0){
            redisFactory.select(RedisDB.MARKET).del(dict.name());
            redisFactory.select(RedisDB.MARKET).lSet(key, list);
        }
    }

    private CategoryChangeRateModel calcCategoryAverage(CategoryModel category) {
        CategoryStockModel categoryStockModel = loadStockByTypeId(category.getId());
        BigDecimal priceCount = new BigDecimal(0);
        BigDecimal closePriceCount = new BigDecimal(0);
        String maxCode = "";
        BigDecimal changeRateAverage = new BigDecimal(0);
        BigDecimal maxChangeRate = new BigDecimal(-10);
        BigDecimal totalValues = categoryStockModel.getTotalValues();
        int effectiveCount = 0;
        for (String stockCode : categoryStockModel.getStockCodes()) {
            StockRealTimeModel realTimeModel = container.getStock(stockCode);
            if (realTimeModel!=null && realTimeModel.getPrice() != null
                    && realTimeModel.getClosePrice() != null
                    && realTimeModel.getOpenPrice() != null
                    && !realTimeModel.getIsStop()) {
                BigDecimal weight = totalValues.divide(realTimeModel.getTotalValue(), 5, BigDecimal.ROUND_HALF_UP);
                BigDecimal changeRate = realTimeModel.getPrice().subtract(realTimeModel.getClosePrice()).divide(realTimeModel.getClosePrice(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
                if (changeRate.compareTo(maxChangeRate) > 0) {
                    maxChangeRate = changeRate;
                    maxCode = realTimeModel.getCode();
                }
                priceCount = priceCount.add(realTimeModel.getPrice().multiply(weight));
                closePriceCount = closePriceCount.add(realTimeModel.getClosePrice().multiply(weight));
                effectiveCount++;
            }
        }
        if (effectiveCount > 0) {
            try {
                changeRateAverage = (priceCount.subtract(closePriceCount)).divide(closePriceCount, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            } catch (Exception e) {
                changeRateAverage = new BigDecimal(0);
            }
        }
        CategoryChangeRateModel result = new CategoryChangeRateModel();
        result.setId(category.getId());
        result.setTitle(category.getTitle());
        result.setRate(changeRateAverage);
        result.setCode(maxCode);
        return result;
    }

    private CategoryStockModel loadStockByTypeId(String typeId) {
        CategoryStockModel result = new CategoryStockModel();
        Set<String> rows = redisFactory.select(RedisDB.STOCK_TREE_DATA).keys("*" + typeId + "*");
        BigDecimal totalValues = new BigDecimal(0);
        List<String> resultList = new ArrayList<>();
        for (String row : rows) {
            StockTreeModel model = redisFactory.select(RedisDB.STOCK_TREE_DATA).get(row);
            if (0 == model.getType()) {
                resultList.add(model.getStockCode());
                StockRealTimeModel realTimeModel = container.getStock(model.getStockCode());
                if (realTimeModel != null && realTimeModel.getTotalValue() != null) {
                    totalValues = totalValues.add(realTimeModel.getTotalValue());
                }
            }
        }
        result.setTotalValues(totalValues);
        result.setStockCodes(resultList);
        return result;
    }
}
