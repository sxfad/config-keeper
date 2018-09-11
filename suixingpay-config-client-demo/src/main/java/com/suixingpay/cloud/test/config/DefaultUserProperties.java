package com.suixingpay.cloud.test.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月6日 下午6:07:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月6日 下午6:07:52
 */
@Data
@ConfigurationProperties(prefix = "suixingpay.defaultuser")
public class DefaultUserProperties {

    private Long id;

    private String name;
}
