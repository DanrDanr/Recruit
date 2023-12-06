package org.example.recruit.entity;

import lombok.Data;

/**
 * @description: 课程信息
 * @author: 22866
 * @date: 2023/12/4
 **/
@Data
public class Course_info {
    private long id;
    private String courseTitle;//课程名称
    private String lecturer;//授课讲师
    private double courseFees;//课程费用
    private long courseDuration;//课程时长
    private long companyId;//对应公司id
    private long admin_id;//对应账号id
}
