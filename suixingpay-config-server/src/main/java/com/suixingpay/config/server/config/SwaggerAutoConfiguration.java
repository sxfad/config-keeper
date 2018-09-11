package com.suixingpay.config.server.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * swagger2配置
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 下午3:04:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 下午3:04:41
 */
@Slf4j
@EnableSwagger2
@Configuration
public class SwaggerAutoConfiguration {


    @Bean
    public Docket defaultDocket() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("随行付配置中心")
                .description("随行付配置中心").version("1.0.0")
                .license("随行付").licenseUrl("http://vbill.cn")
                .contact(new Contact("jiayu.qiu", "http://vbill.cn",
                        "qiu_jy@suixingpay.com"))
                .termsOfServiceUrl("http://vbill.cn").build();

        List<Predicate<String>> basePath = new ArrayList<>();
        basePath.add(PathSelectors.ant("/**"));
        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList<>();
        excludePath.add(PathSelectors.ant("/error"));
        excludePath.add(PathSelectors.ant("/ops/**"));

        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select();

        String basePackage = "com.suixingpay.config.server";
        if (null != basePackage && basePackage.length() > 0) {
            builder.apis(RequestHandlerSelectors.basePackage(basePackage));
        }

        Docket docket = builder
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath))).build();
        return docket;
    }
}
