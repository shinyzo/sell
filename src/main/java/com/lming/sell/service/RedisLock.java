package com.lming.sell.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j
public class RedisLock {

    public static final int TIMEOUT = 10 * 1000;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     *加锁成功返回true
     * @param key  productid
     * @param value 当前时间戳 + 超时时间
     * @return
     */
    public boolean lock(String key,String value)
    {
        if(redisTemplate.opsForValue().setIfAbsent(key,value))
        {
            return true;
        }
        String currentValue = redisTemplate.opsForValue().get(key);
        /**
         * 锁过期
         * 如果时间戳不为空 且存储的时间小于当前时间
         */
        if(!StringUtils.isEmpty(currentValue)
                && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            // 获取上一个锁的时间
            String oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (!StringUtils.isEmpty(oldValue) && oldValue.equals(value))
            {
                return true;
            }
        }

        return false;
    }


    public void unlock(String key ,String value)
    {
        try
        {
            String currentValue = redisTemplate.opsForValue().get(key);
            if(!StringUtils.isEmpty(currentValue)
                    && currentValue.equals(value))
            {
                redisTemplate.opsForValue().getOperations().delete(key);
            }
        }
        catch (Exception e)
        {
            log.error("【redis分布式锁】- 解锁异常，{}",e);
        }


    }

}
