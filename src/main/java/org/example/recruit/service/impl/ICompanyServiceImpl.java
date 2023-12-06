package org.example.recruit.service.impl;

import org.example.recruit.entity.Company_info;
import org.example.recruit.mapper.CompanyMapper;
import org.example.recruit.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Service
public class ICompanyServiceImpl implements CompanyService {
    private CompanyMapper companyMapper;

    @Autowired
    public ICompanyServiceImpl(CompanyMapper companyMapper) {
        this.companyMapper = companyMapper;
    }

    @Override
    public Company_info findByCompanyInfo(Company_info companyInfo) {
        return companyMapper.findByCompanyInfo(companyInfo);
    }

    @Override
    public Company_info findById(long id) {
        return companyMapper.findById(id);
    }
}
