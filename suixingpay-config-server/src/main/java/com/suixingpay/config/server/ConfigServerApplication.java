package com.suixingpay.config.server;

import com.suixingpay.config.server.filter.NoCacheFilter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.servlet.DispatcherType;
import java.util.Arrays;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:07:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:07:16
 */
@Slf4j
@SpringBootApplication
@MapperScan("com.suixingpay.config.server.mapper")
public class ConfigServerApplication {

    public static void main(String[] args) {
        ApplicationContext context = configureApplication().run(args);
        Environment env = context.getBean(Environment.class);
        String[] profiles = env.getActiveProfiles();
        log.info("active profiles--->" + Arrays.toString(profiles));
        log.info(ConfigServerApplication.class.getSimpleName() + " is sussess!");
    }

    private static SpringApplicationBuilder configureApplication() {
        return new SpringApplicationBuilder().sources(ConfigServerApplication.class).bannerMode(Banner.Mode.OFF);
    }

    @Bean
    public FilterRegistrationBean myFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new NoCacheFilter());
        // registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
        registration.setDispatcherTypes(DispatcherType.REQUEST);
        return registration;
    }
}