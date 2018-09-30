package com.suixingpay.config.client.failureanalyzer;

import com.suixingpay.config.client.exception.UnSetProfileException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 未设置profile异常分析
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 上午11:45:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 上午11:45:32
 */
public class UnSetProfileFailureAnalyzer extends AbstractFailureAnalyzer<UnSetProfileException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, UnSetProfileException cause) {
        return new FailureAnalysis("应用程序未能获取到profile",
                "java -jar xxx.jar --spring.profiles.active=xxx或者设置环境变量：SPRING_PROFILES_ACTIVE。开发环境值为:dev;测试环境值为：test; RC环境值为：rc; 生产环境值为：pord",
                cause);
    }
}
