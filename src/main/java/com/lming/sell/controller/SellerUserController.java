package com.lming.sell.controller;

import com.lming.sell.config.ProjectUrlConfig;
import com.lming.sell.constant.CookieConstant;
import com.lming.sell.constant.RedisConstant;
import com.lming.sell.enums.ResultEnum;
import com.lming.sell.model.SellerInfo;
import com.lming.sell.service.SellerService;
import com.lming.sell.util.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/seller")
public class SellerUserController {


    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SellerService sellerService;

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView login(@RequestParam("openid") String openid, HttpServletResponse resposne, Map<String,Object> map) {

        if (StringUtils.isEmpty(openid)) {

        }
        // 根据tokenid查找用户
        SellerInfo sellerInfo = sellerService.findByOpenid(openid);
        if (null == sellerInfo) {
            log.info("【卖家登录】- 用户openid不存在,openid={}", openid);
            map.put("msg", ResultEnum.LOGIN_FAIL.getMsg());
            map.put("url", "/sell/seller/order/list");
            return new ModelAndView("common/error");
        }
        // 设置token至redis 采用分布式session
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token),
                                        openid,
                                        RedisConstant.EXPIRE_TIME,
                                        TimeUnit.SECONDS);
        // 设置token至Cookie
        CookieUtil.set(resposne, CookieConstant.TOKEN,token,CookieConstant.EXPIRE_TIME);


        // 跳转订单列表页
        return new ModelAndView("redirect:"+ projectUrlConfig.getSell() + "/seller/order/list");
    }


    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,HttpServletResponse resposne, Map<String,Object> map){

        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        if(cookie!=null)
        {
            // 删除redis 对应的token
            redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX,cookie.getValue()));
            // 清除cookie中的token
            CookieUtil.set(resposne,CookieConstant.TOKEN,null,0);
        }

        map.put("msg",ResultEnum.LOGOUT_SUCCESS);
        map.put("url","/sell/seller/order/list");

        // 跳转
        return new ModelAndView("common/success",map);
    }
}
