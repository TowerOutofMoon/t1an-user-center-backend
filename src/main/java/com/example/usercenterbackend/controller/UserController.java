package com.example.usercenterbackend.controller;

import com.example.usercenterbackend.common.BaseErrorCode;
import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.exception.BusinessException;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.model.domain.request.DeleteUserRequest;
import com.example.usercenterbackend.model.domain.request.UserLoginRequest;
import com.example.usercenterbackend.model.domain.request.UserRegisterRequest;
import com.example.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户接口控制类
 *
 * @author hty
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<User> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        System.out.println("user register!");
        if (userRegisterRequest == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String communityCode = userRegisterRequest.getCommunityCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, communityCode))
            throw new BusinessException(BaseErrorCode.PARAMS_NULL_ERROR, "检查用户名、密码、检查密码、社区编号是否为空");
        return userService.userRegister(userAccount, userPassword, checkPassword, communityCode);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        System.out.println("user login!");
        if (userLoginRequest == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(BaseErrorCode.PARAMS_NULL_ERROR, "检查用户名或者密码是否为空");
        return userService.userLogin(userAccount, userPassword, request);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> userLogOut(HttpServletRequest request) {
        System.out.println("user logout!");
        if(request == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        return userService.userLogOut(request);
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        System.out.println("get current users!");
        if(request == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        return userService.getCurrentUser(request);
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        System.out.println("search users!");
        if (request == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        return userService.searchUsers(username, request);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUsers(@RequestBody DeleteUserRequest deleteUserRequest, HttpServletRequest request) {
        System.out.println("delete user!");
        if (request == null)
            throw new BusinessException(BaseErrorCode.REQUEST_NULL_ERROR);
        return userService.deleteUser(deleteUserRequest.getId(), request);
    }
}
