package com.suixingpay.cloud.test.config;

import com.suixingpay.cloud.test.dto.DefaultUserWapper;
import com.suixingpay.cloud.test.dto.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月26日 上午10:39:30
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月26日 上午10:39:30
 */
@Configuration
@EnableConfigurationProperties(DefaultUserProperties.class)
public class ConfigTest {

    @Autowired
    private DefaultUserProperties userProperties;

    @RefreshScope
    @Bean(name = "defaultUser")
    public UserDO defaultUser() {
        UserDO userDO = new UserDO();
        userDO.setId(userProperties.getId());
        userDO.setName(userProperties.getName());
        return userDO;
    }

    // @RefreshScope 这种手动注入的bean, 不需要加@RefreshScope 也会动态刷新
    @Bean
    public DefaultUserWapper defaultUserWapper(@Qualifier("defaultUser") UserDO defaultUser) {
        DefaultUserWapper wapper = new DefaultUserWapper();
        wapper.setUserDO(defaultUser);
        return wapper;
    }
}
