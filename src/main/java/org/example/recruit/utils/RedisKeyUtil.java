package org.example.recruit.utils;

/**
 * 项目里面的所有的redis的key都是通过这个工具栏 来拿，不允许自己随手写一个
 * @description:
 * @author: 22866
 * @date: 2023/11/17
 **/
public class RedisKeyUtil {
    public static  String getSMSRedisKey(String phone){
        return "sms_" + phone;
    }

    public static String  getTokenRedisKey(String token) {
        return "token_"+token;
    }
    public static String  getOrderRedisKey(String order) {
        return "order:"+order;
    }
}
