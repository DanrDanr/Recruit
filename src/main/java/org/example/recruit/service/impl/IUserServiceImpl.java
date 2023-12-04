package org.example.recruit.service.impl;

import org.example.recruit.entity.User_info;
import org.example.recruit.mapper.UserMapper;
import org.example.recruit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/4
 **/
@Service
public class IUserServiceImpl implements UserService {
    private UserMapper userMapper;

    @Autowired
    public IUserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public int add(User_info user) {
        return userMapper.add(user);
    }

    @Override
    public User_info login(String phone, String password) {
        return userMapper.login(phone, password);
    }

    @Override
    public User_info findByPhone(String phone) {
        return userMapper.findByPhone(phone);
    }
}
