package org.example.recruit.service;

import org.example.recruit.entity.Recruit_info;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
public interface RecruitService {
    int add(Recruit_info recruit_info);
    List< Recruit_info > getRecruitList();
    List<Recruit_info> getRecruitInfoByParams(@Param("jobPlace") String jobPlace, @Param("salaryRange") String salaryRange);
    Recruit_info findById(long id);
}
