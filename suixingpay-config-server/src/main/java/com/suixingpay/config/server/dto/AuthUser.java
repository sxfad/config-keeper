package com.suixingpay.config.server.dto;

import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.security.TokenInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午10:15:51
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午10:15:51
 */
@Data
@AllArgsConstructor
public class AuthUser implements TokenInfo {

    private static final long serialVersionUID = 1L;

    private UserDO userDO;

    private String[] roles;

    @Override
    public String[] getRoles() {
        return roles;
    }

}
