package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.common.to.PropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MapPropertySource;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 下午5:08:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 下午5:08:52
 */
@Slf4j
@Order(0)
public class SxfConfigServicePropertySourceLocator implements PropertySourceLocator {

    private static final int TRY_CNT = 3;

    private static final int MIN_VERSION = 0;

    private final ConfigDAO configDAO;

    private SxfConfigClientProperties configClientProperties;

    public SxfConfigServicePropertySourceLocator(ConfigDAO configDAO,
                                                 SxfConfigClientProperties configClientProperties) {
        this.configDAO = configDAO;
        this.configClientProperties = configClientProperties;
    }

    @Override
    public org.springframework.core.env.PropertySource<?> locate(org.springframework.core.env.Environment environment) {
        if (log.isTraceEnabled()) {
            log.trace("locating source ... ...");
        }
        CompositePropertySource composite = new CompositePropertySource("configService");
        PropertySource globalConfig = getGlobalConfig();
        if (null != globalConfig && globalConfig.getVersion() >= MIN_VERSION && null != globalConfig.getSource()
                && !globalConfig.getSource().isEmpty()) {
            composite.addPropertySource(new MapPropertySource(globalConfig.getName(), globalConfig.getSource()));
        }

        PropertySource applicationConfig = getApplicationConfig();
        if (null != applicationConfig && applicationConfig.getVersion() >= MIN_VERSION
                && null != applicationConfig.getSource() && !applicationConfig.getSource().isEmpty()) {
            composite.addFirstPropertySource(
                    new MapPropertySource(applicationConfig.getName(), applicationConfig.getSource()));
        }
        return composite;
    }

    private PropertySource getGlobalConfig() {
        PropertySource globalConfig = null;
        int tryCnt = 0;
        do {
            try {
                globalConfig = configDAO.getGlobalConfig();
            } catch (Throwable e) {
                if (configClientProperties.isFailFast()) {
                    throw e;
                }
            }
            if (null == globalConfig) {
                tryCnt++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            } else {
                break;
            }
        } while (tryCnt < TRY_CNT);
        if (null == globalConfig) {
            globalConfig = configDAO.getGlobalConfigLocalCache();
            log.warn("load remote global config fail and use local cache:{}", null == globalConfig);
        }
        return globalConfig;
    }

    private PropertySource getApplicationConfig() {
        PropertySource applicationConfig = null;
        int tryCnt = 0;
        do {
            try {
                applicationConfig = configDAO.getApplicationConfig();
            } catch (RuntimeException e) {
                if (configClientProperties.isFailFast()) {
                    throw e;
                }
            }
            if (null == applicationConfig) {
                tryCnt++;
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                    Thread.currentThread().interrupt();
                }
            } else {
                break;
            }
        } while (null == applicationConfig && tryCnt < TRY_CNT);
        if (null == applicationConfig) {
            applicationConfig = configDAO.getApplicationConfigLocalCache();
            log.warn("load remote application config fail and use local cache:{}", null == applicationConfig);
        }
        return applicationConfig;
    }
}
