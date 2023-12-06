package org.example.recruit.service;

import org.example.recruit.entity.Course_info;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
public interface CourseService {
    int add(Course_info course_info);
    List<Course_info> getCourseList();
}
