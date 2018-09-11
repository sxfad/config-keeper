package com.suixingpay.config.server.security;

import com.suixingpay.config.server.security.client.ClientTokenRepository;
import com.suixingpay.config.server.security.repository.TokenRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Getter
@Slf4j
public class TokenInterceptor<T extends TokenInfo> extends HandlerInterceptorAdapter {

    private static final PathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final String OPTIONS = "OPTIONS";

    private final ClientTokenRepository clientTokenRepository;

    private final TokenRepository<T> tokenRepository;

    private final TokenProperties tokenProperties;

    public TokenInterceptor(TokenProperties tokenProperties, TokenRepository<T> tokenRepository,
                            ClientTokenRepository clientTokenRepository) {
        this.tokenProperties = tokenProperties;
        this.tokenRepository = tokenRepository;
        this.clientTokenRepository = clientTokenRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestMethod = request.getMethod();
        if (OPTIONS.equals(requestMethod)) {
            if (log.isTraceEnabled()) {
                log.trace("request Method is OPTIONS");
            }
            return true;
        }
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String servletPath = request.getServletPath();
        if (log.isTraceEnabled()) {
            log.trace("servletPath:{}", servletPath);
        }
        /**
         * 从切点上获取目标方法
         */
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        /**
         * 若目标方法忽略了安全性检查，则直接调用目标方法
         */
        if (method.isAnnotationPresent(IgnoreToken.class)) {
            return true;
        }
        // 校验token
        String token = clientTokenRepository.get(request);
        if (log.isDebugEnabled()) {
            log.debug("传入的token值:{}", token);
        }
        if (StringUtils.isEmpty(token)) {
            if (log.isDebugEnabled()) {
                log.debug("当前请求token为is empty");
            }
            setUnAuth(response);
            return false;
        }
        T tokenInfo = tokenRepository.get(token);
        if (null == tokenInfo) {
            if (log.isDebugEnabled()) {
                log.debug("token过期或已失效");
            }
            setUnAuth(response);
            return false;
        }
        tokenRepository.refresh(token, tokenProperties.getTimeout());
        if (method.isAnnotationPresent(PreAuthorize.class)) {
            PreAuthorize authorize = method.getAnnotation(PreAuthorize.class);
            if (!hasRole(authorize, tokenInfo)) {
                setForbidden(response);
                return false;
            }
        }
        TokenHelper.setTokenWapper(request, new TokenWapper(token, tokenInfo));
        return true;
    }

    private boolean hasRole(PreAuthorize authorize, TokenInfo tokenInfo) {
        String[] auths = authorize.value();
        if (null != auths && auths.length > 0) {
            String[] roles = tokenInfo.getRoles();
            if (null == roles || roles.length == 0) {
                return false;
            }
            for (String auth : auths) {
                for (String role : roles) {
                    if (null != auth && auth.equals(role)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    private void setUnAuth(HttpServletResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private void setForbidden(HttpServletResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
    }
}
