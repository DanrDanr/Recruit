package org.example.recruit.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.example.recruit.entity.RecruitCollection;
import org.example.recruit.entity.Recruit_info;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/6
 **/
@Mapper
@Repository
public interface RecruitCollectionMapper {
    @Insert("insert into RecruitCollection(user_id,recruit_id,collectionTime)" +
            "values(#{user_id},#{recruit_id},#{collectionTime})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    int add(RecruitCollection recruitCollection);
    @Select("SELECT * FROM RecruitCollection WHERE recruit_id=#{recruit_id} and user_id=#{user_id}")
    RecruitCollection findByUserAndRecruit(long recruit_id,long user_id);
}
