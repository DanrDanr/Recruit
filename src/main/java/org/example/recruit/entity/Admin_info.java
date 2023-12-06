package org.example.recruit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 管理端账号信息
 * @author: 22866
 * @date: 2023/12/4
 **/
@Data
public class Admin_info implements Serializable {
    private long id;
    private String phone;
    private String password;
    private Company_info company_info;//对应公司
    private long company_id;//对应公司
    private String code;
    private String token;
}
