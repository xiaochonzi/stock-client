package com.zhiyun.stock.utils;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.zhiyun.stock.consts.Consts;
import com.zhiyun.stock.consts.Protocol;
import com.zhiyun.stock.properties.AppInfoProperties;
import com.zhiyun.stock.properties.ServerProperties;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * @program: Common
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/3 14:28
 **/
public class Common {

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 包装发送的数据
    public static String dataForWrite(String protocol, JSONObject data) {
        String v = "";
        JSONObject obj = new JSONObject();
        long t = System.currentTimeMillis();
        String dataStr = data.toString();
        AppInfoProperties properties = AppInfoProperties.getInstance();
        String token = MD5(dataStr + t + properties.getAppKey() + properties.getAppSecret());
        try {
            dataStr = URLEncoder.encode(dataStr, "UTF-8");
            obj.put("protocol", protocol);
            obj.put("data", dataStr);
            obj.put("app_key", properties.getAppKey() == null ? "" : properties.getAppKey());
            obj.put("t", t);
            obj.put("token", token);
            v = ZipUtils.zip(obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        v += Consts.writeEnd;
        return v;
    }

    public static String login() {
        JSONObject jsonObject = new JSONObject();
        AppInfoProperties properties = AppInfoProperties.getInstance();
        if (properties.getAppId() != null && properties.getAppPassword() != null) {
            jsonObject.put("app_id", properties.getAppId());
            jsonObject.put("app_password", MD5(properties.getAppPassword()));
            return dataForWrite(Protocol.protocol_checklogin, jsonObject);
        } else {
            return dataForWrite(Protocol.protocol_checkclient, jsonObject);
        }
    }

    public static ServerProperties getServerIp(String serverUri) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(serverUri).path("/socketserverip.php");
        String serverInfo = HttpUtil.get(uriComponentsBuilder.toUriString(), 3000);
        if (!StringUtils.isEmpty(serverInfo)) {
            if (serverInfo.indexOf(":") >= 0) {
                serverInfo = serverInfo.trim();
                serverInfo = serverInfo.replace("\n", "");
                serverInfo = serverInfo.replace("\r", "");
                String[] s = serverInfo.split(":");
                String server = s[0];
                String port = s[1];
                ServerProperties properties = new ServerProperties(server, Integer.parseInt(port));
                return properties;
            }
        }
        return null;
    }

    public static String getKlineServer(String serverUri) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(serverUri).path("/klineserverip.php");
        String url = HttpUtil.get(uriComponentsBuilder.toUriString(), 3000);
        if (url.startsWith("http")) {
            return url;
        }
        return null;
    }
}
