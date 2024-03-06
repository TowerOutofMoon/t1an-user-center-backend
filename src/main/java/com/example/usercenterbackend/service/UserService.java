package com.example.usercenterbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.model.domain.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author hp
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-02-23 15:37:31
 */
public interface UserService extends IService<User> {
    /**
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @param communityCode 社区编号
     * @return 携带脱敏新用户信息的基础响应类
     */
    BaseResponse<User> userRegister(String userAccount, String userPassword, String checkPassword, String communityCode);

    /**
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @return 携带脱敏用户信息的基础响应类
     */
    BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * @param request 请求体
     * @return 当前登录用户信息
     */
    BaseResponse<User> getCurrentUser(HttpServletRequest request);

    /**
     * @param username 用户名
     * @param request  请求体
     * @return 携带脱敏用户信息的基础响应类
     */
    BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request);


    BaseResponse<Integer> userLogOut(HttpServletRequest request);

    /**
     * @param id      用户编号
     * @param request 请求体
     * @return 用户名对应的所有用户信息
     */
    BaseResponse<Boolean> deleteUser(long id, HttpServletRequest request);
}
