package com.example.usercenterbackend.constant;

public class UserConstant {

    public static final String[] NON_SENSITIVE_FIELD = {"id", "communityCode", "username", "gender",
            "userAccount", "avatarUrl", "createTime", "userStatus", "userRole", "email", "phone"};

    public static final String USER_LOGIN_STATE = "user-login-state";
    public static final String USER_PASSWORD_REGEX = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*\\W).*$";

    public static final int SUCCESS_INTEGER = 1;
    public static final int USER_ACCOUNT_MINIMUM_LENGTH = 4;
    public static final int USER_PASSWORD_MINIMUM_LENGTH = 8;
    public static final int COMMUNITY_CODE_MAXIMUM_LENGTH = 5;
    public static final int COMMUNITY_CODE_MINIMUM_LENGTH = 2;
    public static final int ROLE_USER = 0;
    public static final int ROLE_ADMIN = 1;

    public static final long SERIAL_VERSION_UID = 3191241716373120793L;
}
