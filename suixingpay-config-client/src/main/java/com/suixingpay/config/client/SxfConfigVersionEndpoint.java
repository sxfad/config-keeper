package com.suixingpay.config.client;

import com.suixingpay.config.client.SxfConfigVersionEndpoint.VersionInfo;
import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;
import lombok.Data;
import org.springframework.boot.actuate.endpoint.Endpoint;

/**
 * 用于查看配置版本信息
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月21日 下午5:19:08
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月21日 下午5:19:08
 */
public class SxfConfigVersionEndpoint implements Endpoint<VersionInfo> {

    private final ConfigDAO configDAO;

    public SxfConfigVersionEndpoint(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    @Override
    public String getId() {
        return "configversion";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isSensitive() {
        return false;
    }

    @Override
    public VersionInfo invoke() {
        VersionInfo info = new VersionInfo();
        VersionDTO cache = new VersionDTO();
        PropertySource s = configDAO.getApplicationConfigLocalCache();
        cache.setApplicationConfigVersion(null == s ? -1 : s.getVersion());
        s = configDAO.getGlobalConfigLocalCache();
        cache.setGlobalConfigVersion(null == s ? -1 : s.getVersion());
        info.setVersion(configDAO.getVersion());
        info.setLocalVersion(cache);
        return info;
    }

    @Data
    public static class VersionInfo {
        private VersionDTO version;
        private VersionDTO localVersion;
    }
}
