package com.suixingpay.config.server.security;

import java.io.Serializable;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午11:42:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午11:42:04
 */
public interface TokenInfo extends Serializable {

    /**
     * TODO
     *
     * @return
     */
    String[] getRoles();
}
