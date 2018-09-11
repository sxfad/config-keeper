package com.suixingpay.config.server.util;

/**
 * 密码工具
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午10:32:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午10:32:42
 */
public class PasswordUtil {
    /**
     * md5(用户密码+PASSWORD_SALT_KEY)保存到数据库中。
     */
    private static final String PASSWORD_SALT_KEY = "SuiXingpayVBill_@&*()%%$";

    /**
     * 生成MD5加密后的密码
     *
     * @param password
     * @return
     */
    public static String getMd5Password(String password) {
        if (null == password || password.trim().length() == 0) {
            return null;
        }
        return MessageDigestUtil.getMD5(password.trim() + PASSWORD_SALT_KEY);
    }
}
