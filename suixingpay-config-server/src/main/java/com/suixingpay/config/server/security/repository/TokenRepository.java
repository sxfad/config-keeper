package com.suixingpay.config.server.security.repository;

/**
 * token信息仓库
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午10:51:21
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午10:51:21
 */
public interface TokenRepository<T> {
    /**
     * 根据key获取token
     *
     * @param key
     * @return
     */
    T get(String key);

    /**
     * 设置token
     *
     * @param key
     * @param value
     * @param timeout 单位秒
     */
    void set(String key, T value, int timeout);

    /**
     * delete
     *
     * @param key
     */
    void del(String key);

    /**
     * 刷新
     *
     * @param key
     * @param timeout 单位秒
     */
    void refresh(String key, int timeout);
}
