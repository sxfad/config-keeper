package com.suixingpay.config.common.util;

/**
 * 配置中心Rabbit使用的常量
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月11日 下午4:12:03
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月11日 下午4:12:03
 */
public class RabbitUtil {
    /**
     * 配置刷新 exchange
     */
    public static final String CONFIG_REFRESH_EXCHANGE = "suixingpay-config-refresh";

    public static final String CONFIG_REFRESH_QUEUE_PREFIX = "config-refresh";

    private static final String APPLICATION_CONFIG_ROUTING_KEY_FORMAT = "application:%s;profile:%s";

    /**
     * 生成应用配置routingKey
     *
     * @param applicationName
     * @param profile
     * @return
     */
    public static String getApplicationGlobalConfigRoutingKey(String applicationName, String profile) {
        return String.format(APPLICATION_CONFIG_ROUTING_KEY_FORMAT, applicationName, profile);
    }
}
