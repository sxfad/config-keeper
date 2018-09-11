package com.suixingpay.config.server.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午1:49:56
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午1:49:56
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "suixingpay.security")
public class BasicAuthorizationProperties {
    /**
     * 拦截路径
     **/
    String[] pathPatterns = new String[]{"/**"};
    /**
     * 用户名
     **/
    private String username;
    /**
     * 密码
     **/
    private String password;
}
