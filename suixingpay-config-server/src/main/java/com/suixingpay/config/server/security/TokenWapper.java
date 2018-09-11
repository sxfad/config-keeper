package com.suixingpay.config.server.security;

import lombok.Getter;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月14日 上午9:51:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月14日 上午9:51:55
 */
@Getter
public class TokenWapper {
    private final String token;
    private final TokenInfo tokenInfo;

    public TokenWapper(String token, TokenInfo tokenInfo) {
        this.token = token;
        this.tokenInfo = tokenInfo;
    }
}
