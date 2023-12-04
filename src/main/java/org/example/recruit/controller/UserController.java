package org.example.recruit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.recruit.common.ErrorMessage;
import org.example.recruit.common.NetCode;
import org.example.recruit.common.URL;
import org.example.recruit.entity.NetResult;
import org.example.recruit.entity.Result;
import org.example.recruit.entity.User_info;
import org.example.recruit.service.impl.IUserServiceImpl;
import org.example.recruit.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/4
 **/
@RestController
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private RedisTemplate redisTemplate;
    private IUserServiceImpl iUserService;

    @Autowired
    public UserController(RedisTemplate redisTemplate, IUserServiceImpl iUserService) {
        this.redisTemplate = redisTemplate;
        this.iUserService = iUserService;
    }
    /**
     * 短信发送验证码
     *
     * @param phone
     * @return
     * @throws Exception
     */
    @GetMapping(URL.SMS_SEND_CODE)
    public NetResult SMSSendCode(@RequestParam String phone) {
        /**
         * 排除手机号是空的状态
         */
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_NULL, ErrorMessage.PASSWORD_NULL);
        }
        /**
         * 排除手机号格式不正确
         */
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, ErrorMessage.PHONE_INVALID);
        }
        String code = "123456";
        String smsResult = AliSendSMSUtil.sendSMS(code, phone);
        if (smsResult == null) {
            return ResultGenerator.genFailResult("发送验证码失败！");
        }
        // 将新的验证码存入缓存
        redisTemplate.opsForValue().set(RedisKeyUtil.getSMSRedisKey(phone), code, 300, TimeUnit.SECONDS);
        return ResultGenerator.genSuccessResult(Result.fromJsonString(smsResult));
    }

    /**
     * 用户注册
     *
     * @return
     */
    @GetMapping(URL.USER_REGISTER)
    public NetResult register(@RequestBody User_info user_info) {
        String phone = user_info.getPhone();
        // 排除电话未空的状态
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_NULL, ErrorMessage.PHONE_NULL);
        }
        // 排除密码为空的状态
        String password = user_info.getPassword();
        if (StringUtil.isEmpty(password)) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_NULL, ErrorMessage.PASSWORD_NULL);
        }
        // 排除手机格式不正确
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, ErrorMessage.PHONE_INVALID);
        }
        //排除验证码为空的状态
        String code = user_info.getCode();
        if (StringUtil.isEmpty(code)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_NULL, ErrorMessage.CODE_NULL);
        }
        // 尝试从缓存中获取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getSMSRedisKey(phone));
        logger.info(cachedCode);
        if (!StringUtil.isEmpty(cachedCode)) {
            if (code.equals(cachedCode)) {
                //check phone 给人的感觉是检查手机号，而不是判断手机号是否注册
                User_info user = iUserService.findByPhone(phone);
                if(user!=null){
                    // 排除手机号码已注册的状态
                    return ResultGenerator.genErrorResult(NetCode.PHONE_OCCUPATION,ErrorMessage.PHONE_OCCUPATION);
                }
                user_info.setPassword(MD5Util.MD5Encode(password,"utf-8"));
                iUserService.add(user_info);//注册
                user_info.setPassword("");
                return ResultGenerator.genSuccessResult(user_info);
            } else {
                return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, ErrorMessage.CODE_ERROR);
            }
        } else {
            return ResultGenerator.genErrorResult(NetCode.CODE_LAPSE, ErrorMessage.CODE_LAPSE);
        }
    }

    /**
     * 用户登陆
     * @param
     */
    @PostMapping(URL.USER_LOGIN)
    public NetResult UserLogin(@RequestBody User_info user_info) throws JsonProcessingException {
        String phone = user_info.getPhone();
        String code = user_info.getCode();
        String password = user_info.getPassword();
        //排除号码为账号为空的情况
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_NULL, ErrorMessage.PHONE_NULL);
        }
        // 排除手机格式不正确
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, ErrorMessage.PHONE_INVALID);
        }
        //排除密码为null的状态
        if (StringUtil.isEmpty(password)) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_NULL, ErrorMessage.PASSWORD_NULL);
        }
        //排除验证码为空的状态
        if (StringUtil.isEmpty(code)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_NULL, ErrorMessage.CODE_NULL);
        }
        // 尝试从缓存中获取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getSMSRedisKey(phone));
        if (!StringUtil.isEmpty(cachedCode)) {
            if (code.equals(cachedCode)) {
                //登陆的密码  是 传到后端，然后后端去做md5然后去检验，
                // 一般的情况下，都是前端用md5去加密密码，然后 将加密的密码传输给后台，
                //然后后台直接判断   这样就避免了密码的 明文传输
                String psw = MD5Util.MD5Encode(password, "utf-8");
                    User_info u = iUserService.login(phone, password);
                    if (u != null) {
                        String token = UUID.randomUUID().toString();
                        logger.info("token->" + token);
                        redisTemplate.opsForValue().set(RedisKeyUtil.getTokenRedisKey(token), u, 180, TimeUnit.MINUTES);
                        u.setToken(token);
                        u.setPassword(null);
                        return ResultGenerator.genSuccessResult(u);
                    }
                    return ResultGenerator.genFailResult("账号或密码错误");
            } else {
                return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, ErrorMessage.CODE_ERROR);
            }
        }
        return ResultGenerator.genErrorResult(NetCode.CODE_LAPSE, ErrorMessage.CODE_LAPSE);
    }

}
