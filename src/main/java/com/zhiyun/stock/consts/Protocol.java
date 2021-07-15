package com.zhiyun.stock.consts;

/**
 * @program: Protocol
 * @description:
 * @author: Chonzi.xiao
 * @create: 2021/7/3 14:33
 **/
public class Protocol {
    // 注册
    public static String protocol_register = "protocol_register";
    // 合法性验证协议
    public static String protocol_checkclient = "protocol_checkclient";
    // 登陆协议
    public static String protocol_checklogin = "protocol_checklogin";
    // 心跳
    public static String protocol_heart = "protocol_heart";
    // 发送实时行情的key键名数据
    public static String protocol_json_names = "protocol_json_names";
    // 发送实时行情数据
    public static String protocol_realtime = "protocol_realtime";
    // 发送当天日K数据
    public static String protocol_todayKline = "protocol_todayKline";
    // 发送当天日K数据 前复权
    public static String protocol_todayKline_before = "protocol_todayKline_before";
    // 发送当天日K数据 后复权
    public static String protocol_todayKline_after = "protocol_todayKline_after";
    // 发送当天日1分钟数据
    public static String protocol_today1MinuteKline = "protocol_today1MinuteKline";
    // 发送当天日1分钟数据 前复权
    public static String protocol_today1MinuteKline_before = "protocol_today1MinuteKline_before";
    // 发送当天日1分钟数据 后复权
    public static String protocol_today1MinuteKline_after = "protocol_today1MinuteKline_after";
    // 发送当天日5分钟数据
    public static String protocol_today5MinuteKline = "protocol_today5MinuteKline";
    // 发送当天日5分钟数据 前复权
    public static String protocol_today5MinuteKline_before = "protocol_today5MinuteKline_before";
    // 发送当天日5分钟数据 后复权
    public static String protocol_today5MinuteKline_after = "protocol_today5MinuteKline_after";
    // 发送股票分类表
    public static String protocol_allstocks = "protocol_allstocks";
    // 发送股票搜索表
    public static String protocol_searchstocks = "protocol_searchstocks";
    // 发送股票行业分类表
    public static String protocol_hangye_tables = "protocol_hangye_tables";
    // 发送股票概念分类表
    public static String protocol_gainian_tables = "protocol_gainian_tables";
    // 发送股票地区分类表
    public static String protocol_diqu_tables = "protocol_diqu_tables";
}
