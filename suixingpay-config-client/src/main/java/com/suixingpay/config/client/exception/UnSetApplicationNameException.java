package com.suixingpay.config.client.exception;

/**
 * bootstrap.yml未设置spring.application.name
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 下午1:04:12
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 下午1:04:12
 */
public class UnSetApplicationNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnSetApplicationNameException(String message) {
        super(message);
    }

}
