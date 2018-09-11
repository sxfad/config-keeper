package com.suixingpay.config.client.failureanalyzer;

import com.suixingpay.config.client.exception.UnSetApplicationNameException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

/**
 * 未设置spring.application.name异常分析
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月25日 上午11:45:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月25日 上午11:45:32
 */
public class UnSetApplicationNameFailureAnalyzer extends AbstractFailureAnalyzer<UnSetApplicationNameException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, UnSetApplicationNameException cause) {
        return new FailureAnalysis("应用程序没有设置spring.application.name",
                "请在bootstrap.yml中设置spring.application.name，作用应用的唯一标识。", cause);
    }
}
