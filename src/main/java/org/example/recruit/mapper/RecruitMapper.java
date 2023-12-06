package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.Recruit_info;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Mapper
@Repository
public interface RecruitMapper {
    @Insert("insert into Recruit_info(jobTitle,jobPlace,salaryRange,companyId,admin_id)" +
            "values(#{jobTitle},#{jobPlace},#{salaryRange},#{companyId},#{admin_id})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(Recruit_info recruit_info);
    @Select("SELECT * FROM Recruit_info")
    List< Recruit_info > getRecruitList();

    @Select("<script>" +
            "SELECT * FROM recruit_info WHERE 1=1" +
            "<if test='jobPlace != null and !\"\".equals(jobPlace)'> AND jobPlace = #{jobPlace}</if>" +
            "<if test='salaryRange != null and !\"\".equals(salaryRange)'> AND salaryRange = #{salaryRange}</if>" +
            "</script>")
    List<Recruit_info> getRecruitInfoByParams(@Param("jobPlace") String jobPlace, @Param("salaryRange") String salaryRange);

    @Select("SELECT * FROM Recruit_info WHERE id=#{id}")
    Recruit_info findById(long id);
}
