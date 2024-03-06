package com.example.usercenterbackend.model.domain.request;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

import static com.example.usercenterbackend.constant.UserConstant.SERIAL_VERSION_UID;

/**
 * 用户注册请求体
 *
 * @author hty
 */
@Data
public class UserRegisterRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = SERIAL_VERSION_UID;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
    private String communityCode;
}
