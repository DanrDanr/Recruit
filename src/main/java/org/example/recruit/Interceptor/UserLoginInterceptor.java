package org.example.recruit.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.recruit.common.ErrorMessage;
import org.example.recruit.common.NetCode;
import org.example.recruit.entity.NetResult;
import org.example.recruit.utils.RedisKeyUtil;
import org.example.recruit.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: 22866
 * @date: 2023/11/7
 **/
public class UserLoginInterceptor implements HandlerInterceptor {
    private  Logger logger = LoggerFactory.getLogger(UserLoginInterceptor.class);
    private RedisTemplate redisTemplate;

    public UserLoginInterceptor(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    /**
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在处理器处理请求之前执行
        logger.info("执行拦截器！！->"+request.getRequestURL().toString());
        // 因为设置设定了taken令牌 而且一般令牌都是存放在请求头里
        // 那么久查看请求头里taken字段
        String token = request.getHeader("token");
        logger.info("拦截token->"+token);
        //判断taken是否为空 null表示非法请求将拦截 有数据就是正常通过
        if(!StringUtil.isEmpty(token)){
            //在redis里获取该taken对应的用户
           Object o = (Object) redisTemplate.opsForValue().get(RedisKeyUtil.getTokenRedisKey(token));
           logger.info(o.toString());
            if(StringUtil.isEmpty(o.toString())){
                //表示该令牌已过期
                handleFalseResponse(response, NetCode.TOKEN_LAPSE, ErrorMessage.TOKEN_LAPSE,null);
                return false;
            }else {
                //刷新token
                redisTemplate.opsForValue().set(token,o,30, TimeUnit.MINUTES);
                //如果有数据就通过
                return true;
            }
        }
        // 进行拦截 后面接口不执行
        handleFalseResponse(response,NetCode.TOKEN_INVALID,ErrorMessage.TOKEN_INVALID,null);
        return false;
    }

    private void handleFalseResponse(HttpServletResponse response,int code,String msg,Object date) throws Exception {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // 因为设置的是json格式所以写入的数据也必须是json格式
        NetResult netResult = new NetResult();
        netResult.setResultCode(code);
        netResult.setMessage(msg);

        // 转换NetResult对象为JSON字符串
        ObjectMapper objectMapper = new ObjectMapper();
        String netResultJson = objectMapper.writeValueAsString(netResult);

        // 将JSON字符串写入response
        response.getWriter().write(netResultJson);
        response.getWriter().flush();
        response.getWriter().close();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在处理器处理请求完成后，返回ModelAndView之前执行。
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //在DispatchServlet完全处理完请求后执行
    }
}
