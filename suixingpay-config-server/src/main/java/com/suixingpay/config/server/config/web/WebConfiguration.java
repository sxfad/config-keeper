/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年11月27日 11时31分
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.config.server.config.web;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 解决 前端页面打包至server，刷新页面404。若是前后端分离去除此配置！
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年11月27日 11时31分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年11月27日 11时31分
 */
@Configuration
public class WebConfiguration {

    @Bean
    public ErrorPageRegistrar errorPageRegistrar(){
        return new WebErrorPageRegistrar();
    }

    public static class WebErrorPageRegistrar implements ErrorPageRegistrar {
        @Override
        public void registerErrorPages(ErrorPageRegistry registry) {
            ErrorPage indexPage = new ErrorPage(HttpStatus.NOT_FOUND, "/");
            registry.addErrorPages(indexPage);
        }
    }
}
