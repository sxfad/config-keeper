package com.suixingpay.config.server.exception;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 下午2:58:16
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 下午2:58:16
 */
public class ForbiddenException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ForbiddenException(String msg) {
        super(msg);
    }
}
