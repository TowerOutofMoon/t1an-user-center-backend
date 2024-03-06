package com.example.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础响应类
 *
 * code: 业务状态码
 * data: 返回数据
 * message: 返回消息
 * description: 业务状态描述
 * @param <T> 返回数据的类型
 */
@Data
public class BaseResponse<T> implements Serializable {
    private int code;
    private T data;
    private String message;
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, String message, String description) {
        this.code = code;
        this.data = null;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = "";
    }


    public BaseResponse(int code, String message) {
        this.code = code;
        this.data = null;
        this.message = message;
        this.description = "";
    }
}
