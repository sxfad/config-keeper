package com.suixingpay.config.server.security;

import com.suixingpay.config.server.util.MessageDigestUtil;
import com.suixingpay.config.server.util.MiscUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 登录token生成工具
 *
 * @author jiayu.qiu
 */
@Slf4j
public class TokenHelper {

    private static final String ATTRIBUTE_NAME = "MY_CURRENT_TOKEN";

    /**
     * 生成基础的登录 Token
     *
     * @param applicationName
     * @param userId          用户id
     * @return 返回Token
     */
    public static String genToken(String applicationName, Integer userId) {
        String token = null;
        try {
            token = MessageDigestUtil.getMD5(MiscUtil.getRandomStr(5, true) + applicationName + userId
                    + System.currentTimeMillis() + MiscUtil.getRandomStr(5, true));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return token;
    }

    /**
     * 设置当前登录用户
     *
     * @param tokenWapper
     */
    public static void setTokenWapper(HttpServletRequest request, TokenWapper tokenWapper) {
        request.setAttribute(ATTRIBUTE_NAME, tokenWapper);
    }

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static Optional<TokenWapper> getTokenWapper(HttpServletRequest request) {
        Object tmp = request.getAttribute(ATTRIBUTE_NAME);
        if (null != tmp) {
            TokenWapper tokenWapper = (TokenWapper) tmp;
            return Optional.of(tokenWapper);
        }
        return Optional.empty();
    }
}
