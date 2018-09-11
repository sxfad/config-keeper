package com.suixingpay.config.client.exception;

/**
 * 连接配置中心失败
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 上午11:24:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 上午11:24:48
 */
public class ConfigConnectionFailureException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String configServerUri;

    public ConfigConnectionFailureException(String configServerUri, Exception ex) {
        super(ex.getMessage());
        this.configServerUri = configServerUri;
    }

    public String getConfigServerUri() {
        return configServerUri;
    }

}
