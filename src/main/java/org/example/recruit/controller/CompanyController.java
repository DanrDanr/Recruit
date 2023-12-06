package org.example.recruit.controller;

import org.example.recruit.common.ErrorMessage;
import org.example.recruit.common.NetCode;
import org.example.recruit.common.URL;
import org.example.recruit.entity.*;
import org.example.recruit.service.impl.IAdminServiceImpl;
import org.example.recruit.service.impl.ICompanyServiceImpl;
import org.example.recruit.service.impl.ICourseServiceImpl;
import org.example.recruit.service.impl.IRecruitServiceImpl;
import org.example.recruit.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 22866
 * @date: 2023/12/5
 **/
@RestController
@RequestMapping("/company")
public class CompanyController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    private RedisTemplate redisTemplate;
    private IAdminServiceImpl iAdminService;
    private ICompanyServiceImpl iCompanyService;
    private IRecruitServiceImpl iRecruitService;
    private ICourseServiceImpl iCourseService;

    @Autowired
    public CompanyController(RedisTemplate redisTemplate, IAdminServiceImpl iAdminService,
                             ICompanyServiceImpl iCompanyService, IRecruitServiceImpl iRecruitService,
                             ICourseServiceImpl iCourseService) {
        this.redisTemplate = redisTemplate;
        this.iAdminService = iAdminService;
        this.iCompanyService = iCompanyService;
        this.iRecruitService = iRecruitService;
        this.iCourseService = iCourseService;
    }

    /**
     * 公司注册登陆
     *
     * @return
     */
    @GetMapping(URL.MANAGE_REGISTER_AND_REGISTER)
    public NetResult register(@RequestBody Admin_info admin_info) {
        String phone = admin_info.getPhone();
        // 排除电话未空的状态
        if (StringUtil.isEmpty(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_NULL, ErrorMessage.PHONE_NULL);
        }
        // 排除密码为空的状态
        String password = admin_info.getPassword();
        if (StringUtil.isEmpty(password)) {
            return ResultGenerator.genErrorResult(NetCode.PASSWORD_NULL, ErrorMessage.PASSWORD_NULL);
        }
        // 排除手机格式不正确
        if (!RegexUtil.isPhoneValid(phone)) {
            return ResultGenerator.genErrorResult(NetCode.PHONE_INVALID, ErrorMessage.PHONE_INVALID);
        }
        //排除验证码为空的状态
        String code = admin_info.getCode();
        if (StringUtil.isEmpty(code)) {
            return ResultGenerator.genErrorResult(NetCode.CODE_NULL, ErrorMessage.CODE_NULL);
        }
        // 尝试从缓存中获取验证码
        String cachedCode = (String) redisTemplate.opsForValue().get(RedisKeyUtil.getSMSRedisKey(phone));
        logger.info(cachedCode);
        if (!StringUtil.isEmpty(cachedCode)) {
            if (code.equals(cachedCode)) {
                //判断手机号是否注册
                Admin_info admin = iAdminService.findByPhone(phone);
                if (admin != null) {
                    //如果admin存在 那就表示该号码已经注册 那就对比账号密码登陆
                    String psw = MD5Util.MD5Encode(password, "utf-8");
                    Admin_info a = iAdminService.login(phone, psw);
                    if (a != null) {
                        String token = UUID.randomUUID().toString();
                        logger.info("token->" + token);
                        redisTemplate.opsForValue().set(RedisKeyUtil.getTokenRedisKey(token), a, 180, TimeUnit.MINUTES);
                        a.setToken(token);
                        Company_info company = iCompanyService.findById(admin.getCompany_id());
                        a.setCompany_info(company);
                        a.setPassword(null);
                        CodeResBean codeResBean = new CodeResBean<>();
                        codeResBean.msg = "登陆成功";
                        codeResBean.v = a;
                        return ResultGenerator.genSuccessResult(codeResBean);
                    }
                    return ResultGenerator.genFailResult("账号或密码错误");
                }
                admin_info.setPassword(MD5Util.MD5Encode(password, "utf-8"));
                //注册参数检查
                Company_info companyInfo = admin_info.getCompany_info();
                if (companyInfo == null) {
                    return ResultGenerator.genErrorResult(NetCode.COMPANY_INFO_NULL, ErrorMessage.COMPANY_INFO_NULL);
                }
                //公司名不能为空
                if (StringUtil.isEmpty(companyInfo.getCompanyName())) {
                    return ResultGenerator.genErrorResult(NetCode.COMPANY_NAME_NULL, ErrorMessage.COMPANY_NAME_NULL);
                }
                //营业执照号码不能为空
                if (StringUtil.isEmpty(companyInfo.getLicenseNumber())) {
                    return ResultGenerator.genErrorResult(NetCode.LICENSE_NUMBER_NULL, ErrorMessage.LICENSE_NUMBER_NULL);
                }
                //地址不能为空
                if (StringUtil.isEmpty(companyInfo.getAddress())) {
                    return ResultGenerator.genErrorResult(NetCode.ADDRESS_NULL, ErrorMessage.ADDRESS_NULL);
                }
                //法人不能为空
                if (StringUtil.isEmpty(companyInfo.getLegalPerson())) {
                    return ResultGenerator.genErrorResult(NetCode.LEGAL_PERSON_NULL, ErrorMessage.LEGAL_PERSON_NULL);
                }
                //验证公司是否真实存在
                Company_info company_info = iCompanyService.findByCompanyInfo(companyInfo);
                if (company_info == null) {
                    return ResultGenerator.genErrorResult(NetCode.COMPANY_NOT_EXIST, ErrorMessage.COMPANY_NOT_EXIST);
                }
                admin_info.setCompany_info(company_info);
                iAdminService.add(admin_info);//注册
                admin_info.setPassword(null);
                CodeResBean codeResBean = new CodeResBean<>();
                codeResBean.msg = "注册成功";
                codeResBean.v = admin_info;
                return ResultGenerator.genSuccessResult(codeResBean);
            } else {
                return ResultGenerator.genErrorResult(NetCode.CODE_ERROR, ErrorMessage.CODE_ERROR);
            }
        } else {
            return ResultGenerator.genErrorResult(NetCode.CODE_LAPSE, ErrorMessage.CODE_LAPSE);
        }
    }

    /**
     * 发布招聘
     *
     * @param request
     * @return
     */
    @PostMapping(URL.MANAGE_PUBLISH_RECRUIT)
    public NetResult publishRecruit(@RequestBody Recruit_info recruit_info, HttpServletRequest request) {
        String jobTitle = recruit_info.getJobTitle();//职位名称
        String jobPlace = recruit_info.getJobPlace();//工作地点
        String salaryRange = recruit_info.getSalaryRange();//薪资范围
        // 排除工作名称为空的状态
        if (StringUtil.isEmpty(jobTitle)) {
            return ResultGenerator.genErrorResult(NetCode.JOB_TITLE_NULL, ErrorMessage.JOB_TITLE_NULL);
        }
        // 排除工作地点为空的状态
        if (StringUtil.isEmpty(jobPlace)) {
            return ResultGenerator.genErrorResult(NetCode.JOB_PLACE_NULL, ErrorMessage.JOB_PLACE_NULL);
        }
        // 排除薪资范围未空的状态
        if (StringUtil.isEmpty(salaryRange)) {
            return ResultGenerator.genErrorResult(NetCode.SALARY_RANGE_NULL, ErrorMessage.SALARY_RANGE_NULL);
        }
        //通过token获取公司登陆的信息
        String token = request.getHeader("token");
        Admin_info adminInfo = (Admin_info) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (adminInfo == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_UNUSUAL, ErrorMessage.TOKEN_UNUSUAL);
        }
        logger.info("admin->" + adminInfo);
        long companyId = adminInfo.getCompany_id();//对应公司id
        long admin_id = adminInfo.getId();//对应账号id
        recruit_info.setCompanyId(companyId);
        recruit_info.setAdmin_id(admin_id);
        int count = iRecruitService.add(recruit_info);
        if (count == 1) {
            return ResultGenerator.genSuccessResult("发布招聘成功");
        }
        return ResultGenerator.genErrorResult(NetCode.PUBLISH_RECRUIT_LOSE, ErrorMessage.PUBLISH_RECRUIT_LOSE);
    }

    /**
     * 发布课程
     *
     * @param request
     * @return
     */
    @PostMapping(URL.MANAGE_PUBLISH_COURSE)
    public NetResult publishCourse(@RequestBody Course_info course_info, HttpServletRequest request) {
        String courseTitle = course_info.getCourseTitle();//课程名称
        String lecturer = course_info.getLecturer();//授课讲师
        double courseFees = course_info.getCourseFees();//课程费用
        long courseDuration = course_info.getCourseDuration();//课程时长
        // 排除课程名称为空的状态
        if (StringUtil.isEmpty(courseTitle)) {
            return ResultGenerator.genErrorResult(NetCode.COURSE_TITLE_NULL, ErrorMessage.COURSE_TITLE_NULL);
        }
        // 排除授课讲师为空的状态
        if (StringUtil.isEmpty(lecturer)) {
            return ResultGenerator.genErrorResult(NetCode.LECTURER_NULL, ErrorMessage.LECTURER_NULL);
        }
        // 排除课程费用异常状态
        if (courseFees <= 0) {
            return ResultGenerator.genErrorResult(NetCode.COURSE_FEES_UNUSUAL, ErrorMessage.COURSE_FEES_UNUSUAL);
        }
        if (courseDuration <= 0) {
            return ResultGenerator.genErrorResult(NetCode.COURSE_DURATION_UNUSUAL, ErrorMessage.COURSE_DURATION_UNUSUAL);
        }
        //通过token获取公司登陆的信息
        String token = request.getHeader("token");
        Admin_info adminInfo = (Admin_info) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
        if (adminInfo == null) {
            return ResultGenerator.genErrorResult(NetCode.TOKEN_UNUSUAL, ErrorMessage.TOKEN_UNUSUAL);
        }
        logger.info("admin->" + adminInfo);
        long companyId = adminInfo.getCompany_id();//对应公司id
        long admin_id = adminInfo.getId();//对应账号id
        course_info.setCompanyId(companyId);
        course_info.setAdmin_id(admin_id);
        int count = iCourseService.add(course_info);
        if (count == 1) {
            return ResultGenerator.genSuccessResult("发布课程成功");
        }
        return ResultGenerator.genErrorResult(NetCode.PUBLISH_COURSE_LOSE, ErrorMessage.PUBLISH_COURSE_LOSE);
    }
}
