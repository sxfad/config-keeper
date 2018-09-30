package com.suixingpay.config.client.exception;

/**
 * 未设置profile异常
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 上午11:43:18
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 上午11:43:18
 */
public class UnSetProfileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnSetProfileException(String message) {
        super(message);
    }
}
