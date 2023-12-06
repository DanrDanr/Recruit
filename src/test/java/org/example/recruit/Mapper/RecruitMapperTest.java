package org.example.recruit.Mapper;

import org.example.recruit.entity.Recruit_info;
import org.example.recruit.mapper.RecruitMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/6
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class RecruitMapperTest {
    private Logger logger = LoggerFactory.getLogger(RecruitMapperTest.class);
    @Autowired
    private RecruitMapper recruitMapper;
    @Test
    public void list(){
        List< Recruit_info>recruitInfoList=recruitMapper.getRecruitInfoByParams("武汉","");
        logger.info(recruitInfoList.toString());
    }
}
