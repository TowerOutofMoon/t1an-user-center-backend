package com.example.usercenterbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.usercenterbackend.mapper.UserMapper;
import com.example.usercenterbackend.model.domain.User;
import org.springframework.stereotype.Service;

/**
* @author hp
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-03-06 14:54:18
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IService<User> {

}




