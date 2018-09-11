package com.suixingpay.config.client;

import com.suixingpay.config.client.exception.UnSetApplicationNameException;
import com.suixingpay.config.client.exception.UnSetProfileException;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月8日 下午4:04:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午4:04:20
 */
@Data
@ConfigurationProperties(SxfConfigClientProperties.PREFIX)
public class SxfConfigClientProperties {

    public static final String PREFIX = "suixingpay.config";

    private static final String BOOTSTRAP = "bootstrap";

    private static final String APPLICATION = "application";

    /**
     * 应用名称
     */
    private String name;

    /**
     * 运行环境
     */
    private String profile;

    /**
     * 是否启用配置中心. 默认 true;
     */
    private boolean enabled = true;

    /**
     * 配置中心URI
     */
    private List<String> uris;

    /**
     * 配置文件本地缓存路径
     */
    private String cachePath = "./config";

    /**
     * 本地缓存过期时间(单位：秒),如果小于等于0时，一直有效
     */
    private int cacheTimeOut = 0;

    /**
     * 快速失败
     */
    private boolean failFast = false;

    /**
     * 用户名
     **/
    private String username;

    /**
     * 密码
     **/
    private String password = "";

    public SxfConfigClientProperties(Environment environment) {
        String[] profiles = environment.getActiveProfiles();
        if (profiles.length > 0) {
            this.setProfile(profiles[0]);
        }
        String applicationName = environment.getProperty("spring.application.name");
        this.setName(applicationName);
        if (null == profile || profile.length() == 0) {
            throw new UnSetProfileException("profile is empty!");
        }
        if (null == name || name.length() == 0 || BOOTSTRAP.equals(name) || APPLICATION.equals(name)) {
            throw new UnSetApplicationNameException("application name is empty!");
        }
    }
}
