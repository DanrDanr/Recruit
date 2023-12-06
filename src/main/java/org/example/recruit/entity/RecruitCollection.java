package org.example.recruit.entity;

import lombok.Data;

/**
 * @description:招聘收藏
 * @author: 22866
 * @date: 2023/12/6
 **/
@Data
public class RecruitCollection {
    private long id;
    private long user_id;//收藏用户id
    private long recruit_id;//收藏招聘id
    private long collectionTime;//收藏时间
}
