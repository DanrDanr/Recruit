package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.Admin_info;
import org.example.recruit.entity.Company_info;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Mapper
@Repository
public interface CompanyMapper {
    @Select("select * from company_info where companyName=#{companyName} and " +
            "licenseNumber=#{licenseNumber} and address=#{address}" +
            "and legalPerson=#{legalPerson}")
    Company_info findByCompanyInfo(Company_info companyInfo);

    @Select("SELECT * FROM company_info WHERE id = #{id}")
    Company_info findById(long id);
}
