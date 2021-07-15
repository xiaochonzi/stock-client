package com.zhiyun.stock.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @program: JsonUtils
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/6 00:01
 **/
public class JsonUtils {

    public static JSONObject strToJsonObject(String value, JSONArray names) {
        String[] v = value.split(",");
        JSONObject obj = new JSONObject();
        String[] ns = sortWithKeys(names.toString());
        for (int i = 0; i < v.length; i++) {
            String f = v[i];
            try {
                if (i < ns.length) {
                    String key = ns[i];
                    obj.put(key, f);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    /**
     * 字符串数组排序
     *
     * @param keys key1,key2,key3....
     * @return
     */
    public static String[] sortWithKeys(String keys) {
        keys = keys.replace("\"", "");
        keys = keys.replace("[", "");
        keys = keys.replace("]", "");
        String[] str = keys.split(",");
        arraySort(str);
        return str;
    }

    public static void arraySort(String[] array) {
        // 从此一个位开始循环数组
        for (int i = 0; i < array.length; i++) {
            // 从第i+1为开始循环数组
            for (int j = i + 1; j < array.length; j++) {
                String stri = array[i].substring(0, 1);
                String strj = array[j].substring(0, 1);
                int leni = array[i].length();
                int lenj = array[j].length();
                if (stri.compareTo(strj) > 0) {
                    String tem = array[i];
                    array[i] = array[j];
                    array[j] = tem;
                } else if (stri.compareTo(strj) == 0) {
                    // 如果相等就比较长度
                    if (leni > lenj) {
                        String tem = array[i];
                        array[i] = array[j];
                        array[j] = tem;
                    } else if (leni == lenj) {
//                        System.out.println("长度相等："+array[i]+"=="+array[j]);
                        // 如果相同就按照查找
                        char[] ci = array[i].toCharArray();
                        char[] cj = array[j].toCharArray();
                        for (int k = 0; k < ci.length; k++) {

                            if (ci[k] > cj[k]) {
//                                System.out.println("比较字母："+ci[k]+">"+cj[k]);
                                String tem = array[i];
                                array[i] = array[j];
                                array[j] = tem;
                                break;
                            } else if (ci[k] < cj[k]) {
                                break;
                            }
                        }

                    }
                }
            }
        }
    }

    public static JSONObject fixNull(JSONObject jsonObject) {
        for (String key : jsonObject.keySet()) {
            String value = jsonObject.getString(key).trim();
            if (StrUtil.equals("NaN", value) || StrUtil.equals("Infinity", value)) {
                jsonObject.put(key, null);
            } else {
                jsonObject.put(key, value);
            }
        }
        return jsonObject;
    }
}
