package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.common.BaseErrorCode;
import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.common.ResponseHelper;
import com.example.usercenterbackend.exception.BusinessException;
import com.example.usercenterbackend.mapper.UserMapper;
import com.example.usercenterbackend.model.domain.User;
import com.example.usercenterbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.usercenterbackend.constant.UserConstant.*;
import static com.example.usercenterbackend.utils.EncryptionUtils.encryptToMD5;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-03-06 14:54:18
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public BaseResponse<User> userRegister(String userAccount, String userPassword, String checkPassword, String communityCode) {
        // 1.注册校验
        // 1.1 非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, communityCode))
            throw new BusinessException(BaseErrorCode.PARAMS_NULL_ERROR);
        // 1.2 长度限制
        if (userAccount.length() < USER_ACCOUNT_MINIMUM_LENGTH)
            throw new BusinessException(BaseErrorCode.ACCOUNT_LENGTH_ERROR);
        if (userPassword.length() < USER_PASSWORD_MINIMUM_LENGTH)
            throw new BusinessException(BaseErrorCode.PASSWORD_LENGTH_ERROR);
        if (communityCode.length() > COMMUNITY_CODE_MAXIMUM_LENGTH || communityCode.length() < COMMUNITY_CODE_MINIMUM_LENGTH)
            throw new BusinessException(BaseErrorCode.COMMUNITY_CODE_LENGTH_ERROR, "社区编号过长或者过短(2-5位)");
        // 1.3 必须包含数字字母和字符
        if (!userPassword.matches(USER_PASSWORD_REGEX))
            throw new BusinessException(BaseErrorCode.PASSWORD_REGEX_ERROR, "必须同时包含数字字母和特殊字符(!@#$%^&*" );
        // 1.4 校验密码和输入密码相同
        if (!userPassword.equals(checkPassword))
            throw new BusinessException(BaseErrorCode.CHECK_INCONSISTENCY_ERROR);
        // 1.5 账户必须唯一
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("userAccount", userAccount);
        if (userMapper.selectCount(qw) > 0)
            throw new BusinessException(BaseErrorCode.ACCOUNT_UNIQUE_ERROR);
        // 1.6 社区编号必须唯一
        qw = new QueryWrapper<>();
        qw.eq("communityCode", communityCode);
        if (userMapper.selectCount(qw) > 0)
            throw new BusinessException(BaseErrorCode.COMMUNITY_CODE_UNIQUE_ERROR);
        // 2.加密注册
        String encryptedPassword = encryptToMD5(userPassword);
        // 3.插入用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptedPassword);
        user.setCommunityCode(communityCode);
        if (!this.save(user))
            throw new BusinessException(BaseErrorCode.INSERT_ERROR);
        // 4.返回创建的用户信息
        User createdUser = desensitizeUser(user);
        // 5.封装成基础响应类
        return ResponseHelper.successResponse(createdUser, "用户注册成功！");
    }

    @Override
    public BaseResponse<User> userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1.登录校验
        // 1.1 输入数据非空
        if (StringUtils.isAnyBlank(userAccount, userPassword))
            throw new BusinessException(BaseErrorCode.PARAMS_NULL_ERROR);
        // 1.2 长度限制
        if (userAccount.length() < USER_ACCOUNT_MINIMUM_LENGTH)
            throw new BusinessException(BaseErrorCode.ACCOUNT_LENGTH_ERROR);
        if (userPassword.length() < USER_PASSWORD_MINIMUM_LENGTH)
            throw new BusinessException(BaseErrorCode.PASSWORD_LENGTH_ERROR);
        // 1.3 必须包含数字字母和字符
        if (!userPassword.matches(USER_PASSWORD_REGEX))
            throw new BusinessException(BaseErrorCode.PASSWORD_REGEX_ERROR, "必须同时包含数字字母和特殊字符(!@#$%^&*" );
        // 2.加密
        String encryptedPassword = encryptToMD5(userPassword);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("userAccount", userAccount);
        qw.eq("userPassword", encryptedPassword);
        System.out.println(userAccount + " " + encryptedPassword);
        // 3.处理结果
        User selectedUser = userMapper.selectOne(qw);
        System.out.println("selectedUser " + selectedUser);
        if (selectedUser == null) {
            log.info("User log failed, the username and password do not match!");
            throw new BusinessException(BaseErrorCode.QUERY_ERROR, "用户名和密码不匹配");
        }
        // 4.脱敏
        User safetyUser = desensitizeUser(selectedUser);
//        System.out.println("safetyUser " + safetyUser);
        // 5.记录登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        // 6.封装成基础响应类
        return ResponseHelper.successResponse(safetyUser, "用户登录成功！");
    }

    @Override
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        // 1.取出session中的对象
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null)
            throw new BusinessException(BaseErrorCode.NO_AUTH_ERROR, "请先进行登录操作");
        // 2.脱敏
        User selectedUser = userMapper.selectById(user.getId());
        if (selectedUser == null)
            throw new BusinessException(BaseErrorCode.QUERY_ERROR, "未查询到对应用户");
        User safetyUser = desensitizeUser(selectedUser);
        // 3.封装成基础响应类
        return ResponseHelper.successResponse(safetyUser, "成功获取到当前用户!");
    }

    @Override
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        // 1.鉴权
        if (isCasualUser(request))
            throw new BusinessException(BaseErrorCode.NO_AUTH_ERROR, "不是管理员，无法进行查询操作");
        // 2.模糊查询,用户名为空时查询全部
        QueryWrapper<User> qw = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) qw.like("username", username);
        // 3.用户脱敏
        List<User> userList = this.list(qw);
        if (userList == null || userList.size() == 0)
            throw new BusinessException(BaseErrorCode.QUERY_ERROR, "没有查询到对应用户列表");
        List<User> safetyUserList = userList.stream().map(this::desensitizeUser).collect(Collectors.toList());
        // 4.封装成基础响应类
        return ResponseHelper.successResponse(safetyUserList, "成功查询到用户列表!");
    }

    @Override
    public BaseResponse<Integer> userLogOut(HttpServletRequest request) {
        // 1.移除session对应状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        // 2.封装成基础响应类
        return ResponseHelper.successResponse(SUCCESS_INTEGER, "成功注销登录!");
    }

    @Override
    public BaseResponse<Boolean> deleteUser(long id, HttpServletRequest request) {
        System.out.println("id" + id);
        // 1.基础校验
        if (id < 0)
            throw new BusinessException(BaseErrorCode.DELETE_ERROR, "不存在这样的id");
        // 2.鉴权操作
        if (isCasualUser(request))
            throw new BusinessException(BaseErrorCode.NO_AUTH_ERROR, "不是管理员，无法进行删除操作");
        // 3.删除操作
        if (!this.removeById(id))
            throw new BusinessException(BaseErrorCode.DELETE_ERROR, "未能成功删除");
        // 4.封装成基础响应类
        return ResponseHelper.successResponse(true, "成功删除用户!");
    }

    /**
     * 通过获取请求中Session的状态获取User信息，判断其是否为普通用户
     *
     * @param request 用户请求
     * @return 是否为普通用户
     */
    private boolean isCasualUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        return user != null && user.getUserRole() == ROLE_USER;
    }

    /**
     * 将敏感信息隐藏，返回安全的用户对象
     *
     * @param user 用户对象
     * @return 脱敏后的用户对象
     */
    private User desensitizeUser(User user) {
        User safetyUser = new User();
        for (String attr : NON_SENSITIVE_FIELD) {
            Field f;
            try {
                f = User.class.getDeclaredField(attr);
                f.setAccessible(true);
                Object value = f.get(user);
                f.set(safetyUser, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return safetyUser;
    }
}




