package org.example.recruit.service.impl;

import org.example.recruit.entity.RecruitCollection;
import org.example.recruit.mapper.RecruitCollectionMapper;
import org.example.recruit.service.RecruitCollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/6
 **/
@Service
public class IRecruitCollectionServiceImpl implements RecruitCollectionService {
    private RecruitCollectionMapper collectionMapper;

    @Autowired
    public IRecruitCollectionServiceImpl(RecruitCollectionMapper collectionMapper) {
        this.collectionMapper = collectionMapper;
    }

    @Override
    public int add(RecruitCollection recruitCollection) {
        return collectionMapper.add(recruitCollection);
    }

    @Override
    public RecruitCollection findByUserAndRecruit(long recruit_id, long user_id) {
        return collectionMapper.findByUserAndRecruit(recruit_id, user_id);
    }

}
