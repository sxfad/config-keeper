package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;

/**
 * 本地缓存版本和远程版本进行比较，如果版本不一样，则视为down
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月21日 下午3:25:30
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月21日 下午3:25:30
 */
public class SxfConfigServerHealthIndicator extends AbstractHealthIndicator {

    private ConfigDAO configDAO;

    public SxfConfigServerHealthIndicator(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    @Override
    protected void doHealthCheck(Builder builder) throws Exception {
        VersionDTO versionTO = this.configDAO.getVersion();
        if (null == versionTO) {
            builder.down().withDetail("error", "获取远程版本信息失败");
            return;
        }
        PropertySource cache = this.configDAO.getApplicationConfigLocalCache();
        if (null == cache) {
            builder.down().withDetail("error", "应用配置本地缓存为null");
            return;
        }
        if (versionTO.getApplicationConfigVersion() != cache.getVersion()) {
            builder.unknown().withDetail("error", "应用配置配置中心已经被修改");
            return;
        }

        cache = this.configDAO.getGlobalConfigLocalCache();
        if (null == cache) {
            builder.down().withDetail("error", "全局配置本地缓存为null");
            return;
        }
        if (versionTO.getGlobalConfigVersion() != cache.getVersion()) {
            builder.unknown().withDetail("error", "全局配置配置中心已经被修改");
            return;
        }
        builder.up().withDetail("version", versionTO);
    }
}
