package org.example.recruit.controller;

import org.example.recruit.common.ErrorMessage;
import org.example.recruit.common.NetCode;
import org.example.recruit.common.URL;
import org.example.recruit.entity.*;
import org.example.recruit.service.impl.ICourseServiceImpl;
import org.example.recruit.service.impl.IRecruitCollectionServiceImpl;
import org.example.recruit.service.impl.IRecruitServiceImpl;
import org.example.recruit.service.impl.IUserServiceImpl;
import org.example.recruit.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    private IRecruitServiceImpl iRecruitService;
    private ICourseServiceImpl iCourseService;
    private IRecruitCollectionServiceImpl collectionService;

    @Autowired
    public UserController(RedisTemplate redisTemplate, IUserServiceImpl iUserService,
                          IRecruitServiceImpl iRecruitService, ICourseServiceImpl iCourseService,
                          IRecruitCollectionServiceImpl collectionService) {
        this.redisTemplate = redisTemplate;
        this.iUserService = iUserService;
        this.iRecruitService = iRecruitService;
        this.iCourseService = iCourseService;
        this.collectionService = collectionService;
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
     * 用户注册登陆
     *
     * @return
     */
    @GetMapping(URL.USER_REGISTER_AND_REGISTER)
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
                //判断手机号是否注册
                User_info user = iUserService.findByPhone(phone);
                if (user != null) {
                    //如果user存在 那就表示该号码已经注册 那就对比账号密码登陆
                    String psw = MD5Util.MD5Encode(password, "utf-8");
                    User_info u = iUserService.login(phone, psw);
                    if (u != null) {
                        String token = UUID.randomUUID().toString();
                        logger.info("token->" + token);
                        redisTemplate.opsForValue().set(RedisKeyUtil.getTokenRedisKey(token), u, 180, TimeUnit.MINUTES);
                        u.setToken(token);
                        u.setPassword(null);
                        return ResultGenerator.genSuccessResult(u);
                    }
                    return ResultGenerator.genFailResult("账号或密码错误");
                }
                user_info.setPassword(MD5Util.MD5Encode(password, "utf-8"));
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
     * 用户查看 课程列表或者招聘列表
     *
     * @param type
     * @return
     */
    @GetMapping(URL.USER_GET_RECRUIT_COURSE)
    public NetResult register(@RequestParam int type) {
        if (type == 0) {//招聘列表
            List< Recruit_info > recruitInfoList = iRecruitService.getRecruitList();
            return ResultGenerator.genSuccessResult(recruitInfoList);
        }
        if (type == 1) {//课程列表
            List< Course_info > courseInfoList = iCourseService.getCourseList();
            return ResultGenerator.genSuccessResult(courseInfoList);
        }
        return ResultGenerator.genErrorResult(NetCode.TYPE_UNUSUAL, ErrorMessage.TYPE_UNUSUAL);
    }

    /**
     * 根据参数筛选查询
     *
     * @param jobPlace
     * @param salaryRange
     * @return
     */
    @GetMapping(URL.USER_GET_RECRUIT_BY_PRAM)
    public NetResult getRecruitInfoByParams(@RequestParam(required = false) String jobPlace,
                                            @RequestParam(required = false) String salaryRange) {
        if (StringUtil.isEmpty(jobPlace) && StringUtil.isEmpty(salaryRange)) {
            return ResultGenerator.genErrorResult(NetCode.PRAM_NULL, ErrorMessage.PRAM_NULL);
        }
        List< Recruit_info > recruitInfoList = iRecruitService.getRecruitInfoByParams(jobPlace, salaryRange);
        return ResultGenerator.genSuccessResult(recruitInfoList);
    }

    /**
     * 查询招聘详情
     *
     * @param id
     * @return
     */
    @GetMapping(URL.USER_GET_RECRUIT_BY_ID)
    public ResponseEntity< NetResult > getRecruitInById(@RequestParam String id) {
        if (StringUtil.isEmpty(id)) {
            return ResponseEntity.badRequest().body(ResultGenerator.genErrorResult(NetCode.PRAM_NULL, ErrorMessage.PRAM_NULL));
        }
        Recruit_info recruitInfo = iRecruitService.findById(Long.parseLong(id));
        if (recruitInfo == null) {
            return ResponseEntity.badRequest().body(ResultGenerator.genErrorResult(NetCode.RECRUIT_NULL, ErrorMessage.RECRUIT_NULL));
        }
        return ResponseEntity.ok(ResultGenerator.genSuccessResult(recruitInfo));
    }

    @PostMapping(URL.USER_COLLECTION_RECRUIT)
    public NetResult getRecruitInfoByParams(@RequestParam String recruit_id, HttpServletRequest request) {
        // 查看参数是否为空 这个是收藏id
        if (StringUtil.isEmpty(recruit_id)) {
            return ResultGenerator.genErrorResult(NetCode.PRAM_NULL, ErrorMessage.PRAM_NULL);
        }
        long recruitId = Long.parseLong(recruit_id);
        Recruit_info recruit_info = iRecruitService.findById(recruitId);
        // 查看该招聘存不存在
        if (recruit_info == null) {
            return ResultGenerator.genErrorResult(NetCode.RECRUIT_NULL, ErrorMessage.RECRUIT_NULL);
        }
        //通过token获取用户的信息
        String token = request.getHeader("token");
        User_info user_info = (User_info) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (user_info == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_UNUSUAL, ErrorMessage.TOKEN_UNUSUAL);
        }
        long user_id = user_info.getId();
        // 查看用户有没有收藏此条招聘
        RecruitCollection collection = collectionService.findByUserAndRecruit(recruitId, user_id);
        if (collection != null) {
            //已收藏
            return ResultGenerator.genErrorResult(NetCode.BOOKMARKED, ErrorMessage.BOOKMARKED);
        }
        // 确认数据无误 添加数据到招聘收藏表里
        RecruitCollection recruitCollection = new RecruitCollection();
        recruitCollection.setRecruit_id(recruitId);
        recruitCollection.setUser_id(user_id);
        recruitCollection.setCollectionTime(System.currentTimeMillis());
        int count = collectionService.add(recruitCollection);
        if (count == 1) {
            //收藏成功
            return ResultGenerator.genSuccessResult(recruitCollection);
        }
        return ResultGenerator.genErrorResult(NetCode.COLLECTION_LOSE, ErrorMessage.COLLECTION_LOSE);
    }

}
