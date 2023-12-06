package org.example.recruit.service.impl;

import org.example.recruit.entity.Course_info;
import org.example.recruit.mapper.CourseMapper;
import org.example.recruit.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Service
public class ICourseServiceImpl implements CourseService {
    private CourseMapper courseMapper;

    @Autowired
    public ICourseServiceImpl(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    @Override
    public int add(Course_info course_info) {
        return courseMapper.add(course_info);
    }

    @Override
    public List< Course_info > getCourseList() {
        return courseMapper.getCourseList();
    }
}
