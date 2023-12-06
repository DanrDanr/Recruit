package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.Company_info;
import org.example.recruit.entity.Course_info;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Mapper
@Repository
public interface CourseMapper {
    @Insert("insert into Course_info(courseTitle,lecturer,courseFees,courseDuration,companyId,admin_id)" +
            "values(#{courseTitle},#{lecturer},#{courseFees},#{courseDuration},#{companyId},#{admin_id})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Course_info course_info);
    @Select("SELECT * FROM Course_info")
    List<Course_info> getCourseList();
}
