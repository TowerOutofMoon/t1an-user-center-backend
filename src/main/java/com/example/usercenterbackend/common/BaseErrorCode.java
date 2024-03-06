package com.example.usercenterbackend.common;

/**
 * 定义了各种业务错误类型
 */
public enum BaseErrorCode {
    SUCCESS(0, "业务正常", ""),

    PARAMS_NULL_ERROR(40000, "传入参数为空", ""),
    REQUEST_NULL_ERROR(40001, "前端请求为空", ""),

    ACCOUNT_LENGTH_ERROR(40002, "账号长度有误", ""),
    ACCOUNT_UNIQUE_ERROR(40003, "账号已存在", ""),

    PASSWORD_LENGTH_ERROR(40004, "密码长度有误", ""),
    PASSWORD_REGEX_ERROR(40005, "密码格式有误", ""),

    COMMUNITY_CODE_LENGTH_ERROR(40006, "编号长度有误", ""),
    COMMUNITY_CODE_UNIQUE_ERROR(40007, "编号已存在", ""),

    CHECK_INCONSISTENCY_ERROR(40008, "确认密码和原密码不一致", ""),

    QUERY_ERROR(40009, "查询失败", ""),
    INSERT_ERROR(40010, "插入失败", ""),
    DELETE_ERROR(40011, "删除失败", ""),

    NOT_LOGIN_ERROR(40100, "未登录", ""),
    NO_AUTH_ERROR(40101, "无权限", ""),

    SYSTEM_ERROR(50000, "系统内部异常", "");

    /**
     * 业务码
     */
    private final int code;

    /**
     * 业务状态信息
     */
    private final String message;

    /**
     * 业务状态描述（详细）
     */
    private final String description;

    BaseErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
