package org.example.recruit.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

/**
 * @description: 消息短信返回字段
 * @author: 22866
 * @date: 2023/11/14
 **/
@Data
public class SmsMsg {
    private String msg;
    private String smsid;//批次号。可通过该ID查询发送状态或者回复短信。API接口可联系客服获取。
    private String code;
    private String balance;//账户剩余次数
    private String ILLEGAL_WORDS;// 如有则显示
    // 1、http响应状态码对照表请参考：https://help.aliyun.com/document_detail/43906.html；
    // 2、如果次数用完会返回 403，Quota Exhausted，此时继续购买就可以；
    // 3、如果appCode输入不正确会返回 403，Unauthorized；

    public static SmsMsg fromJsonString(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, SmsMsg.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
