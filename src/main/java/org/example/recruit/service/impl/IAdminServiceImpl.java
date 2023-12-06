package org.example.recruit.service.impl;

import org.example.recruit.entity.Admin_info;
import org.example.recruit.mapper.AdminMapper;
import org.example.recruit.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Service
public class IAdminServiceImpl implements AdminService {
    private AdminMapper adminMapper;

    @Autowired
    public IAdminServiceImpl(AdminMapper adminMapper) {
        this.adminMapper = adminMapper;
    }

    @Override
    public int add(Admin_info adminInfo) {
        return adminMapper.add(adminInfo);
    }

    @Override
    public Admin_info login(String phone, String password) {
        return adminMapper.login(phone, password);
    }

    @Override
    public Admin_info findByPhone(String phone) {
        return adminMapper.findByPhone(phone);
    }
}
