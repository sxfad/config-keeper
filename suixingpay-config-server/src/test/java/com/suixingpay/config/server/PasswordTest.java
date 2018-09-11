package com.suixingpay.config.server;

import com.suixingpay.config.server.util.PasswordUtil;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午9:50:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午9:50:04
 */
public class PasswordTest {

    /**
     * TODO
     *
     * @param args
     */
    public static void main(String[] args) {
        String password = "admin";
        String md5 = PasswordUtil.getMd5Password(password);
        System.out.println(md5);
        md5 = PasswordUtil.getMd5Password("test");
        System.out.println(md5);
    }

}
