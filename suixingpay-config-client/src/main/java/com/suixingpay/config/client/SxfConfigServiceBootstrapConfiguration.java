package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.client.dao.ConfigDAOImpl;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.commons.util.UtilAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 下午5:08:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 下午5:08:52
 */
@Configuration
@AutoConfigureAfter(UtilAutoConfiguration.class)
public class SxfConfigServiceBootstrapConfiguration {

    @Autowired
    private ConfigurableEnvironment environment;

    @Bean
    public SxfConfigClientProperties sxfConfigClientProperties(ApplicationContext context) {
        if (context.getParent() != null && BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context.getParent(),
                SxfConfigClientProperties.class).length > 0) {
            return BeanFactoryUtils.beanOfTypeIncludingAncestors(context.getParent(), SxfConfigClientProperties.class);
        }
        return new SxfConfigClientProperties(environment);
    }

    @Bean
    public ConfigDAO sxfConfigDAO(SxfConfigClientProperties sxfConfigClientProperties) {
        return new ConfigDAOImpl(sxfConfigClientProperties);
    }


    @Bean
    @ConditionalOnMissingBean(SxfConfigServicePropertySourceLocator.class)
    @ConditionalOnProperty(value = "suixingpay.config.enabled", matchIfMissing = true)
    public SxfConfigServicePropertySourceLocator sxfConfigServicePropertySource(ApplicationContext context) {
        SxfConfigClientProperties configClientProperties = sxfConfigClientProperties(context);
        ConfigDAO configDAO = sxfConfigDAO(configClientProperties);
        return new SxfConfigServicePropertySourceLocator(configDAO, configClientProperties);
    }

}
