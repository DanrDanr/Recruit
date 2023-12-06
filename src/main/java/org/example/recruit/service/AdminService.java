package org.example.recruit.service;

import org.example.recruit.entity.Admin_info;


/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
public interface AdminService {
    int add(Admin_info adminInfo);
    Admin_info login(String phone, String password);
    Admin_info findByPhone(String phone);
}
