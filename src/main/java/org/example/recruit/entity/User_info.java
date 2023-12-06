package org.example.recruit.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 用户端信息
 * @author: 22866
 * @date: 2023/12/4
 **/
@Data
public class User_info implements Serializable {
    private long id;
    private String phone;
    private String password;
    private String code;
    private String token;
}
