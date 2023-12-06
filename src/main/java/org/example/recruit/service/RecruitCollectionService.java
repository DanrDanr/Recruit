package org.example.recruit.service;

import org.example.recruit.entity.RecruitCollection;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/6
 **/
public interface RecruitCollectionService {
    int add(RecruitCollection recruitCollection);
    RecruitCollection findByUserAndRecruit(long recruit_id,long user_id);
}
