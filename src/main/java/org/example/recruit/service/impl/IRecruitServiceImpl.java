package org.example.recruit.service.impl;

import org.example.recruit.entity.Recruit_info;
import org.example.recruit.mapper.RecruitMapper;
import org.example.recruit.service.RecruitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@Service
public class IRecruitServiceImpl implements RecruitService {
    private RecruitMapper recruitMapper;

    @Autowired
    public IRecruitServiceImpl(RecruitMapper recruitMapper) {
        this.recruitMapper = recruitMapper;
    }

    @Override
    public int add(Recruit_info recruit_info) {
        return recruitMapper.add(recruit_info);
    }

    @Override
    public List< Recruit_info > getRecruitList() {
        return recruitMapper.getRecruitList();
    }

    @Override
    public List< Recruit_info > getRecruitInfoByParams(String jobPlace, String salaryRange) {
        return recruitMapper.getRecruitInfoByParams(jobPlace, salaryRange);
    }

    @Override
    public Recruit_info findById(long id) {
        return recruitMapper.findById(id);
    }
}
