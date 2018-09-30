package com.suixingpay.config.client.dao;

import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月20日 下午3:32:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月20日 下午3:32:14
 */
public interface ConfigDAO {

    String UTF8 = "UTF-8";

    /**
     * 获取版本配置中心配置最新版本信息
     *
     * @return
     */
    VersionDTO getVersion();

    /**
     * 获取全局配置
     *
     * @return
     */
    PropertySource getGlobalConfig();

    /**
     * 从本地缓存中获取数据
     *
     * @return
     */
    PropertySource getGlobalConfigLocalCache();

    /**
     * 获取应用配置
     *
     * @return
     */
    PropertySource getApplicationConfig();

    /**
     * 从本地缓存中获取
     *
     * @return
     */
    PropertySource getApplicationConfigLocalCache();
}
