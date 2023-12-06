package org.example.recruit.service;

import org.example.recruit.entity.Company_info;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
public interface CompanyService {
    Company_info findByCompanyInfo(Company_info companyInfo);
    Company_info findById(long id);
}
