package com.lming.sell.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    /**
     * 设置cookie
     * @param response
     * @param key
     * @param value
     * @param expireTime
     */
    public static void set(HttpServletResponse response,String key,String value,Integer expireTime)
    {
        Cookie cookie = new Cookie(key,value);
        cookie.setPath("/");
        cookie.setMaxAge(expireTime);
        response.addCookie(cookie);
    }

    /**
     * 获取cookie
     * @param request
     * @param key
     * @return
     */
    public static Cookie get(HttpServletRequest request ,String key)
    {
       return readCookieMap(request).get(key);

    }

    /**
     * 将cookie转成map
     * @param request
     * @return
     */
    private static Map<String,Cookie> readCookieMap(HttpServletRequest request)
    {
        Map<String,Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies =  request.getCookies();
        if(cookies!=null)
        {
           for(Cookie cookie : cookies)
           {
               cookieMap.put(cookie.getName(),cookie);
           }
        }

        return cookieMap;
    }


}
