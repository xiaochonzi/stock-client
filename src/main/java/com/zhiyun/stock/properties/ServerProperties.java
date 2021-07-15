package com.zhiyun.stock.properties;

/**
 * @program: ServerProperties
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/5 14:34
 **/
public class ServerProperties {
    private String host;
    private Integer port;


    public ServerProperties(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerProperties{" +
                "host='" + host + '\'' +
                ", port=" + port +
                '}';
    }
}
