package com.suixingpay.config.server.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author: jiayu.qiu[qiu_jy@suixingpay.com]
 * @date: 2017年7月19日 下午10:58:32
 * @version: V1.0
 * @review: jiayu.qiu[qiu_jy@suixingpay.com]/2017年7月19日 下午10:58:32
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "suixingpay.token")
public class TokenProperties {

    String[] pathPatterns = new String[]{"/**"};
    /**
     * token过期时间，单位秒
     */
    private int timeout = 20 * 60;

    /**
     *
     */
    private List<String> ignoreUrls;

}
