package com.suixingpay.config.server.util;

import com.suixingpay.config.server.dto.AuthUser;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.security.TokenHelper;
import com.suixingpay.config.server.security.TokenWapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午10:42:23
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午10:42:23
 */
public class SecurityUtil {
    public static Optional<UserDO> getSessionUser(HttpServletRequest request) {
        Optional<TokenWapper> info = TokenHelper.getTokenWapper(request);
        if (info.isPresent()) {
            TokenWapper tokenWapper = info.get();
            AuthUser authUser = (AuthUser) tokenWapper.getTokenInfo();
            return Optional.of(authUser.getUserDO());
        }
        return Optional.empty();
    }
}
