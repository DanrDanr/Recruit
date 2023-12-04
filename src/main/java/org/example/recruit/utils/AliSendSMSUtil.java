package org.example.recruit.utils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.example.recruit.entity.SmsMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/17
 **/
public class AliSendSMSUtil {
    private static final Logger logger = LoggerFactory.getLogger(AliSendSMSUtil.class);

    public static final String sendSMS(String code,String phone){
        String host = "https://dfsmsv2.market.alicloudapi.com";
        String path = "/data/send_sms_v2";
        String method = "POST";
        String appcode = "dd31c4a2f9014af5b66dd61889cfcfb0";
        Map< String, String > headers = new HashMap< String, String >();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map< String, String > querys = new HashMap< String, String >();
        Map< String, String > bodys = new HashMap< String, String >();
        bodys.put("content", "code:" + code);
        bodys.put("template_id", "TPL_0000");
        bodys.put("phone_number", phone);
        logger.info(code);
        HttpResponse response = null;
        try {
            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        if(response == null){
            return null;
        }


        HttpEntity entity = response.getEntity();
        String result = null;
        if (entity != null) {
            try (InputStream inputStream = entity.getContent()) {
                result = StringUtil.convertStreamToString(inputStream); // 将输入流转换为字符串
                logger.info(result);
                return result;
            } catch (IOException e) {
                // 处理异常
                logger.error(e.getMessage());
                return null;
            }
        }
        return null;
    }


    /**
     * 用户通知店铺的短信
     *
     * @param phone
     * @param name
     */
    public static SmsMsg sendSmsShop(String phone, String name, String userPhone) throws Exception {
        String host = "https://gyyyx1.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "25948b3da7cd41699b37c71c2a70070c";
        Map< String, String > headers = new HashMap< String, String >();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map< String, String > querys = new HashMap< String, String >();
        querys.put("mobile", phone);
        querys.put("templateId", "066285a885974689ab3f78e127a5cc06");
        querys.put("smsSignId", "1596868d15704706bee87cca32639de7");
        querys.put("param", "**name**:" + name + ",**phone**:" + userPhone);
        Map< String, String > bodys = new HashMap< String, String >();
        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        SmsMsg smsMsg = SmsMsg.fromJsonString(responseString);
        logger.info(smsMsg.toString());
        return smsMsg;
    }
    /**
     * 店铺发给用户通知订单已审核
     *
     * @param phone
     * @param name
     * @param
     */
    public static SmsMsg shopSendUser(String phone, String name) throws Exception {
        String host = "https://gyyyx1.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "25948b3da7cd41699b37c71c2a70070c";
        Map< String, String > headers = new HashMap< String, String >();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map< String, String > querys = new HashMap< String, String >();
        querys.put("mobile", phone);
        querys.put("templateId", "0f7b6dcf69a64acea4278fad09a31aee");
        querys.put("smsSignId", "1596868d15704706bee87cca32639de7");
        querys.put("param", "**name**:" + name);
        Map< String, String > bodys = new HashMap< String, String >();

        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        SmsMsg smsMsg = SmsMsg.fromJsonString(responseString);
        return smsMsg;

    }
}
