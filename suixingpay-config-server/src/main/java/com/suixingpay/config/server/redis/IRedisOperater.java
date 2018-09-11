package com.suixingpay.config.server.redis;

/**
 * Created by chiyajing on 2018/5/31.
 */

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Description: redis 公共service
 * @author: zhoumaowang<zhou_mw @ suixingpay.com>
 * @date: 2017/3/22 上午11:01
 * @version: V1.0
 */
@Component
public class IRedisOperater {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取缓存值
     *
     * @param cacheKey
     * @return
     */
    public Object get(String cacheKey) {
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        return cacheValue;
    }

    /**
     * 设置缓存值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }


    /**
     * 设置缓存值并设置有效期
     *
     * @param key
     * @param value
     */
    public void setex(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * 删除key值
     *
     * @param key
     */
    public void delete(String key) {
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    /**
     * 删除keys值
     *
     * @param keys
     */
    public void delete(Set<String> keys) {
        redisTemplate.opsForValue().getOperations().delete(keys);
    }

    /**
     * 获取token的有效期
     *
     * @param key
     */
    public long getExpireTime(String key) {
        long time = redisTemplate.getExpire(key, TimeUnit.SECONDS);
        return time;
    }

    /**
     * 设置token的有效期
     *
     * @param key
     * @return
     */
    public boolean setExpireTime(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 判断可以是否存在
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);

    }

    /**
     * 判断是否过期
     *
     * @param key
     * @return
     */
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);

    }

}

