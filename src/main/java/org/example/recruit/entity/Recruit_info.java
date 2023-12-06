package org.example.recruit.entity;

import lombok.Data;

/**
 * @description: 招聘信息
 * @author: 22866
 * @date: 2023/12/4
 **/
@Data
public class Recruit_info {
    private long id;
    private String jobTitle;//职位名称
    private String jobPlace;//工作地点
    private String salaryRange;//薪资范围
    private long companyId;//对应公司id
    private long admin_id;//对应账号id
}
