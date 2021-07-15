package com.zhiyun.stock.utils;

import cn.hutool.core.collection.CollUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

/**
 * @program: CollectionUtils
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/9 13:19
 **/
public class CollectionUtils extends CollUtil {

    public static Map<String, BigDecimal> sortMapByValue(Map<String, BigDecimal> map) {
        return sortToMap(map.entrySet(), new Comparator<Map.Entry<String, BigDecimal>>() {
            @Override
            public int compare(Map.Entry<String, BigDecimal> o1, Map.Entry<String, BigDecimal> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
    }
}
