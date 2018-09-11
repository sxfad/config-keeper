package com.suixingpay.config.server.security.client;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午9:25:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午9:25:16
 */
public class HeaderRepository implements ClientTokenRepository {

    @Override
    public String get(HttpServletRequest request) {
        return request.getHeader(AUTH_TOKEN);
    }

}
