package com.suixingpay.config.server.security.repository;


import com.suixingpay.config.server.redis.IRedisOperater;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午9:14:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午9:14:09
 */
public class RedisTokenRepository<T> implements TokenRepository<T> {

    private IRedisOperater redisOperater;

    public RedisTokenRepository(IRedisOperater redisOperater) {
        this.redisOperater = redisOperater;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(String key) {
        return (T) redisOperater.get(key);
    }

    @Override
    public void set(String key, T value, int timeout) {
        redisOperater.setex(key, value, timeout);
    }

    @Override
    public void refresh(String key, int timeout) {
        redisOperater.expire(key, timeout);
    }

    @Override
    public void del(String key) {
        redisOperater.delete(key);
    }

}
