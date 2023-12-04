package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.User_info;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/4
 **/
@Mapper
@Repository
public interface UserMapper {
    @Insert("insert into user_info(phone,password)" +
            "values(#{phone},#{password})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(User_info user);

    @Select("select * from user_info where phone=#{phone} and password=#{password}")
    User_info login(String phone, String password);

    @Select("SELECT * FROM user_info WHERE phone = #{phone}")
    User_info findByPhone(String phone);
}
