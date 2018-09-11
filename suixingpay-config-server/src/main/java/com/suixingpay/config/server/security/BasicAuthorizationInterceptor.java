package com.suixingpay.config.server.security;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午1:25:37
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午1:25:37
 */
@Getter
@Slf4j
public class BasicAuthorizationInterceptor extends HandlerInterceptorAdapter {

    private static final String OPTIONS = "OPTIONS";

    private final BasicAuthorizationProperties config;

    private final boolean useBasicAuth;

    public BasicAuthorizationInterceptor(BasicAuthorizationProperties config) {
        this.config = config;
        String username = config.getUsername();
        String password = config.getPassword();
        if (null != username && username.trim().length() > 0 && null != password && password.trim().length() > 0) {
            this.useBasicAuth = true;
        } else {
            this.useBasicAuth = false;
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!this.useBasicAuth) {
            return true;
        }
        String requestMethod = request.getMethod();
        if (OPTIONS.equals(requestMethod)) {
            log.trace("request Method is OPTIONS");
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String auth = request.getHeader("Authorization");
        if (!checkAuth(auth)) {
            setUnAuth(response);
            return false;
        }

        return true;
    }

    private boolean checkAuth(String auth) {
        if (null == auth || auth.trim().length() < 6) {
            return false;
        }
        auth = auth.substring(6);
        byte[] authData = Base64.getDecoder().decode(auth);
        auth = new String(authData);
        String[] tmp = auth.split(":");
        String name = null;
        String password = null;
        if (null == tmp || tmp.length < 2) {
            return false;
        }
        name = tmp[0];
        password = tmp[1];
        if (null != name && name.equals(this.config.getUsername()) && null != password
                && password.equals(this.config.getPassword())) {
            return true;
        }
        return false;
    }

    private void setUnAuth(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

}
