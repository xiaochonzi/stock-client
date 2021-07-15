package com.zhiyun.stock.mvc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("通用返回对象")
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -3948389268046368059L;
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("错误信息")
    private String message;
    @ApiModelProperty("数据")
    private T data;

    public Response() {
    }

    public Response(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Response ok() {
        return ok(null);
    }

    public static <T> Response ok(T data) {
        return new Response(200, "ok", data);
    }

    public static Response error(String message) {
        return error(500, message);
    }

    public static Response error(Integer code, String message) {
        return new Response(code, message, null);
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
