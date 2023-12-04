package org.example.recruit.service;

import org.example.recruit.entity.User_info;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/4
 **/
public interface UserService {
    int add(User_info user);
    User_info login(String phone, String password);
    User_info findByPhone(String phone);
}
