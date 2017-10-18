package com.lming.sell.aspect;

import com.lming.sell.exception.SellerAuthorizeException;
import com.lming.sell.constant.CookieConstant;
import com.lming.sell.constant.RedisConstant;
import com.lming.sell.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户登录切面
 */
@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Pointcut("execution(public * com.lming.sell.controller.Seller*.*(..))"
            + "&& !execution(public * com.lming.sell.controller.SellerUserController.*(..))")
    public void verify(){};


    @Before("verify()")
    public void doVerify(){
       ServletRequestAttributes requestAttributes =  (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
       HttpServletRequest request = requestAttributes.getRequest();

//        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
//        if(cookie==null)
//        {
//            log.warn("【登录校验】-cookie中找不到token");
//            //
//            throw new SellerAuthorizeException();
//        }
//        String token = cookie.getValue();
//        // redis中是否存在改token
//        String openid = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX,token));
//        if(StringUtils.isEmpty(openid))
//        {
//            log.warn("【登录校验】- redis中找不到token，token={}",token);
//            throw new SellerAuthorizeException();
//        }


    }

}
