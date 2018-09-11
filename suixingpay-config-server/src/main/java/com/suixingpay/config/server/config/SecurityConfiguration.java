package com.suixingpay.config.server.config;

import com.suixingpay.config.server.dto.AuthUser;
import com.suixingpay.config.server.redis.IRedisOperater;
import com.suixingpay.config.server.security.BasicAuthorizationInterceptor;
import com.suixingpay.config.server.security.BasicAuthorizationProperties;
import com.suixingpay.config.server.security.TokenInterceptor;
import com.suixingpay.config.server.security.TokenProperties;
import com.suixingpay.config.server.security.client.ClientTokenRepository;
import com.suixingpay.config.server.security.client.HeaderRepository;
import com.suixingpay.config.server.security.repository.RedisTokenRepository;
import com.suixingpay.config.server.security.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午10:50:19
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午10:50:19
 */
@Configuration
@EnableConfigurationProperties({TokenProperties.class, BasicAuthorizationProperties.class})
public class SecurityConfiguration extends WebMvcConfigurerAdapter {
    @Autowired
    private TokenProperties tokenProperties;

    @Autowired
    private BasicAuthorizationProperties basicAuthorizationProperties;

    @Autowired
    private IRedisOperater redisOperater;

    @Bean
    public TokenRepository<AuthUser> tokenRepository() {
        return new RedisTokenRepository<>(redisOperater);
    }

    @Bean
    public ClientTokenRepository clientTokenRepository() {
        return new HeaderRepository();
    }

    @Bean
    public TokenInterceptor<AuthUser> tokenInterceptor() {
        return new TokenInterceptor<AuthUser>(tokenProperties, tokenRepository(), clientTokenRepository());
    }

    @Bean
    BasicAuthorizationInterceptor basicAuthorizationInterceptor() {
        return new BasicAuthorizationInterceptor(basicAuthorizationProperties);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration tokenInterceptorRegistration = registry.addInterceptor(tokenInterceptor()).addPathPatterns(tokenProperties.getPathPatterns());
        List<String> ignoreUrls = tokenProperties.getIgnoreUrls();
        if (null != ignoreUrls && !ignoreUrls.isEmpty()) {
            String[] tmp = new String[ignoreUrls.size()];
            tmp = ignoreUrls.toArray(tmp);
            tokenInterceptorRegistration.excludePathPatterns(tmp);
        }
        registry.addInterceptor(basicAuthorizationInterceptor())
                .addPathPatterns(basicAuthorizationProperties.getPathPatterns());
    }
}
