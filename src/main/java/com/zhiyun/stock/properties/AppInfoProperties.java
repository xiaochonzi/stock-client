package com.zhiyun.stock.properties;

import com.zhiyun.stock.consts.Consts;
import com.zhiyun.stock.utils.FileUtils;

import java.io.*;
import java.util.Properties;

/**
 * @program: AppInfoProperties
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 12:10
 **/
public class AppInfoProperties {
    private String appId = Consts.appId;
    private String appKey = Consts.appKey;
    private String appPassword = Consts.appPassword;
    private String appSecret = Consts.appSecret;

    static AppInfoProperties _instance;

    private AppInfoProperties() {
        Properties properties = new Properties();
        try {
            String filePath = this.getFile();
            if (FileUtils.exist(filePath) && FileUtils.isFile(filePath)) {
                InputStream is = new FileInputStream(new File(filePath));
                properties.load(is);
                this.appId = properties.getProperty("appId");
                this.appKey = properties.getProperty("appKey");
                this.appPassword = properties.getProperty("appPassword");
                this.appSecret = properties.getProperty("appSecret");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppInfoProperties getInstance() {
        if (_instance == null) {
            _instance = new AppInfoProperties();
        }
        return _instance;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    private String getFile() {
        return FileUtils.getProjectPath() + "app.properties";
    }

    public synchronized void save() {
        String filePath = this.getFile();
        File file;
        if (!FileUtils.exist(filePath)) {
            file = new File(filePath);
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            file = new File(filePath);
        }
        Properties properties = new Properties();
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            properties.setProperty("appId", this.appId);
            properties.setProperty("appKey", this.appKey);
            properties.setProperty("appSecret", this.appSecret);
            properties.setProperty("appPassword", this.appPassword);
            properties.store(os, "");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
