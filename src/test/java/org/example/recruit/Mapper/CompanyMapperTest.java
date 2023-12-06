package org.example.recruit.Mapper;

import org.example.recruit.controller.UserController;
import org.example.recruit.entity.Company_info;
import org.example.recruit.mapper.CompanyMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class CompanyMapperTest {
    private Logger logger = LoggerFactory.getLogger(CompanyMapperTest.class);
    @Autowired
    private CompanyMapper companyMapper;

    @Test
    public void findByCompanyInfoTest(){
        Company_info companyInfo = new Company_info();
        companyInfo.setCompanyName("艾尔");
        companyInfo.setLicenseNumber("123456789");
        companyInfo.setAddress("武汉");
        companyInfo.setLegalPerson("某");
        Company_info company_info ;
        try {
            company_info=companyMapper.findByCompanyInfo(companyInfo);
            logger.info(company_info.toString());
        }catch (Exception e){
            logger.info(e.toString());
        }

    }
}
