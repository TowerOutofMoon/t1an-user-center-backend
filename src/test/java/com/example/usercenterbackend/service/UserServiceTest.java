package com.example.usercenterbackend.service;

import com.example.usercenterbackend.common.BaseResponse;
import com.example.usercenterbackend.exception.BusinessException;
import com.example.usercenterbackend.model.domain.User;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.List;

import static com.example.usercenterbackend.constant.UserConstant.USER_LOGIN_STATE;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author hty
 */

@SpringBootTest
class UserServiceTest {
    // 测试之前删除'testUser'
    @Resource
    private UserService userService;

    @Test
    @Order(1)
    void testRegister() {
        // 测试账户信息为空的情况
        assertThrows(BusinessException.class, () -> userService.userRegister(null, null, null, null));

        // 测试账户信息长度不符合要求的情况
        assertThrows(BusinessException.class, () -> userService.userRegister("abc", "123", "123", "1234567"));

        // 测试密码不包含数字、字母和字符的情况
        assertThrows(BusinessException.class, () -> userService.userRegister("testUser", "password", "password", "12345"));

        // 测试密码和确认密码不一致的情况
        assertThrows(BusinessException.class, () -> userService.userRegister("testUser", "Password!123", "Password!124", "12345"));

        // 测试重复的账户信息或社区编号的情况
        assertThrows(BusinessException.class, () -> userService.userRegister("hty1", "Password!123", "Password!123", "12345"));

        // 测试正常注册流程
        assertNotNull(userService.userRegister("testUser", "Password!123", "Password!123", "12345").getData());
    }

    @Test
    @Order(2)
    void testLogin() {
        // 测试输入数据为空的情况
        assertThrows(BusinessException.class, () -> userService.userLogin(null, null, null));

        // 测试账户信息长度不符合要求的情况
        assertThrows(BusinessException.class, () -> userService.userLogin("abc", "123", null));

        // 测试密码长度不符合要求的情况
        assertThrows(BusinessException.class, () -> userService.userLogin("testUser", "pass", null));

        // 测试密码不包含数字、字母和字符的情况
        assertThrows(BusinessException.class, () -> userService.userLogin("testUser", "password", null));

        // 测试用户名和密码不匹配的情况
        // 假设数据库中不存在对应的用户名和密码

        // 测试正常登录流程
        HttpServletRequest request = new MockHttpServletRequest();
        assertNotNull(userService.userLogin("mockUser", "Password!123", request).getData());
    }

    @Test
    @Order(3)
    void testGetCurrentUser() {
        // 测试当前用户为空的情况
        HttpServletRequest request = new MockHttpServletRequest();
        assertThrows(BusinessException.class, () -> userService.getCurrentUser(request));

        // 模拟用户登录
        assertNotNull(userService.userLogin("mockUser", "Password!123", request).getData());

        // 测试正常获取当前用户流程
        User currentUser = userService.getCurrentUser(request).getData();
        assertNotNull(currentUser);
        assertEquals(1L, currentUser.getId());
    }

    @Test
    @Order(4)
    void testSearchUsers() {
        // 测试非管理员用户查询用户列表的情况
        HttpServletRequest request = new MockHttpServletRequest();

        // 模拟管理员用户登录态
        assertNotNull(userService.userLogin("mockUser", "Password!123", request).getData());

        // 测试正常查询用户列表流程
        BaseResponse<List<User>> response = userService.searchUsers("hty", request);
        assertNotNull(response.getData());
        Assertions.assertFalse(response.getData().isEmpty());
    }

    @Test
    @Order(5)
    void testUserLogOut() {
        // 模拟用户登录态
        HttpServletRequest request = new MockHttpServletRequest();
        assertNotNull(userService.userLogin("testUser", "Password!123", request).getData());

        // 测试正常注销登录流程
        BaseResponse<Integer> response = userService.userLogOut(request);
        assertEquals("成功注销登录!", response.getDescription());
        assertNull(request.getSession().getAttribute(USER_LOGIN_STATE));
    }

    @Test
    @Order(6)
    void testDeleteUser() {
        HttpServletRequest request = new MockHttpServletRequest();
        // 模拟管理员登录情况
        assertNotNull(userService.userLogin("mockUser", "Password!123", request).getData());
        // 测试删除不存在用户的情况
        assertThrows(BusinessException.class, () -> userService.deleteUser(-1L, request));

        // 模拟用户登录情况
        User testUser = userService.userLogin("testUser", "Password!123", request).getData();
        assertNotNull(testUser);
        // 获取用户ID
        long deleteId = testUser.getId();

        // 模拟管理员登录情况
        assertNotNull(userService.userLogin("mockUser", "Password!123", request).getData());
        // 测试正常删除用户流程
        BaseResponse<Boolean> deleteResponse = userService.deleteUser(deleteId, request);
        assertTrue(deleteResponse.getData());
    }
}