package com.suixingpay.config.client.failureanalyzer;

import com.suixingpay.config.client.exception.ConfigConnectionFailureException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 上午11:26:36
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 上午11:26:36
 */
public class ConfigConnectionFailureAnalyzer extends AbstractFailureAnalyzer<ConfigConnectionFailureException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, ConfigConnectionFailureException cause) {
        return new FailureAnalysis("访问配置中心失败, 不能访问" + cause.getConfigServerUri(), "请确认网络是否正常，配置中心服务运行是否正常.", cause);
    }

}
