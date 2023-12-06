package org.example.recruit.entity;

import lombok.Data;

/**
 * @description: 公司信息
 * @author: 22866
 * @date: 2023/12/4
 **/
@Data
public class Company_info {
    private long id;
    private String companyName;//公司名称
    private String licenseNumber;//营业执照号码
    private String address;//注册地址
    private String legalPerson;//法人
}
