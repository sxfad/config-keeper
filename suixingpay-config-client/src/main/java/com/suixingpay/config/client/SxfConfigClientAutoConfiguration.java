package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月21日 下午3:33:11
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月21日 下午3:33:11
 */
public class SxfConfigClientAutoConfiguration {

    @Bean
    @ConditionalOnBean(ConfigDAO.class)
    @ConditionalOnClass(Endpoint.class)
    public SxfConfigLocalVersionEndpoint sxfConfigLocalVersionEndpoint(ConfigDAO configDAO) {
        return new SxfConfigLocalVersionEndpoint(configDAO);
    }
}
