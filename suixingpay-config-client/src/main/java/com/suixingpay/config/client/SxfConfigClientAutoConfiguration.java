package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.client.dao.ConfigDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月21日 下午3:33:11
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月21日 下午3:33:11
 */
@Configuration
public class SxfConfigClientAutoConfiguration {

    @Autowired
    private SxfConfigClientProperties configClientProperties;

    @Bean
    @ConditionalOnMissingBean(ConfigDAO.class)
    public ConfigDAO sxfConfigDAO() {
        return new ConfigDAOImpl(configClientProperties);
    }

    @Bean
    @ConditionalOnBean(ConfigDAO.class)
    @ConditionalOnMissingBean(SxfConfigVersionEndpoint.class)
    public SxfConfigVersionEndpoint sxfConfigVersionEndpoint() {
        return new SxfConfigVersionEndpoint(sxfConfigDAO());
    }

    @Configuration
    @ConditionalOnBean(ConfigDAO.class)
    @ConditionalOnClass(HealthIndicator.class)
    protected static class ConfigServerHealthIndicatorConfiguration {

        @Bean
        public SxfConfigServerHealthIndicator configServerHealthIndicator(ConfigDAO configDAO) {
            return new SxfConfigServerHealthIndicator(configDAO);
        }
    }
}
