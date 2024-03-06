package com.example.usercenterbackend.common;

/**
 * 提供了多种便捷构造BaseResponse的方法
 */
public class ResponseHelper {
    /**
     * @param data 响应中返回的数据
     * @param <T>  返回数据的类型
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> successResponse(T data) {
        return new BaseResponse<>(0, data, "success");
    }

    /**
     * @param data        响应中返回的数据
     * @param description 响应的描述
     * @param <T>         返回数据的类型
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> successResponse(T data, String description) {
        return new BaseResponse<>(0, data, "success", description);
    }

    /**
     * @param code        业务状态码
     * @param message     业务消息
     * @param description 业务消息描述
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> failedResponse(int code, String message, String description) {
        return new BaseResponse<>(code, message, description);
    }


    /**
     * @param errorCode   自定义错误码
     * @param message     业务消息
     * @param description 业务消息描述
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> failedResponse(BaseErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), message, description);
    }

    /**
     * @param errorCode 自定义错误码
     * @param message   业务消息
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> failedResponse(BaseErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), message, errorCode.getDescription());
    }

    /**
     * @param errorCode 自定义错误码
     * @return 基本响应类
     */
    public static <T> BaseResponse<T> failedResponse(BaseErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage(), errorCode.getDescription());
    }
}
