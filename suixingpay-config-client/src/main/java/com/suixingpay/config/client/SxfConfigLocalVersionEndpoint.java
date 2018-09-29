package com.suixingpay.config.client;

import com.suixingpay.config.client.dao.ConfigDAO;
import com.suixingpay.config.common.Constant;
import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;
import org.springframework.boot.actuate.endpoint.Endpoint;


/**
 * 用于查看配置版本信息
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月21日 下午5:19:08
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月21日 下午5:19:08
 */
public class SxfConfigLocalVersionEndpoint implements Endpoint<VersionDTO> {
    private static final int UNKOWN_VERSION = -1;
    private final ConfigDAO configDAO;

    public SxfConfigLocalVersionEndpoint(ConfigDAO configDAO) {
        this.configDAO = configDAO;
    }

    @Override
    public String getId() {
        return Constant.GET_CONFIG_VERSION_PATH;
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
    public VersionDTO invoke() {
        VersionDTO cache = new VersionDTO();
        PropertySource propertySources = configDAO.getApplicationConfigLocalCache();
        cache.setApplicationConfigVersion(null == propertySources ? UNKOWN_VERSION : propertySources.getVersion());
        propertySources = configDAO.getGlobalConfigLocalCache();
        cache.setGlobalConfigVersion(null == propertySources ? UNKOWN_VERSION : propertySources.getVersion());
        return cache;
    }
}
