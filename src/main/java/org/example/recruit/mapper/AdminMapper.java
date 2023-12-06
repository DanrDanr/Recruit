package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.Admin_info;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/4
 **/
@Mapper
@Repository
public interface AdminMapper {
    @Insert("insert into admin_info(phone,password,company_id)" +
            "values(#{phone},#{password},#{company_info.id})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Admin_info adminInfo);

    @Select("select * from admin_info where phone=#{phone} and password=#{password}")
    Admin_info login(String phone, String password);

    @Select("SELECT * FROM admin_info WHERE phone = #{phone}")
    Admin_info findByPhone(String phone);
}
