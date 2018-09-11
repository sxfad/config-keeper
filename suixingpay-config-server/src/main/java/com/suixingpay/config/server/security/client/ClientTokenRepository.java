package com.suixingpay.config.server.security.client;

import javax.servlet.http.HttpServletRequest;

/**
 * 从客户端获取token的方法,可以是从header或cookie等方式传递
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午10:27:47
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午10:27:47
 */
public interface ClientTokenRepository {
    String AUTH_TOKEN = "X-Token";

    /**
     * 获取Token
     *
     * @param request
     * @return
     */
    String get(HttpServletRequest request);
}
