package com.suixingpay.config.server.entity;

import com.suixingpay.config.server.enums.SourceType;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月14日 下午3:04:24
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月14日 下午3:04:24
 */
public interface BasePropertySourceDO {

    /**
     * 获取配置类型
     *
     * @return
     */
    SourceType getSourceType();

    /**
     * 获取配置内容
     *
     * @return
     */
    String getPropertySource();

    /**
     * 获取版本号
     *
     * @return
     */
    Integer getVersion();
}
