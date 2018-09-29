package com.suixingpay.config.server.config.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

/**
 * 自定义任务执行异常处理器
 * 
 * @author: wanghongfei[wang_hf@suixingpay.com]
 * @date: 2018年7月24日 上午10:35:09
 * @version: V1.0
 * @review: wanghongfei[wang_hf@suixingpay.com]/2018年7月24日 上午10:35:09
 */
@Slf4j
public class SimpleAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

    private AsyncProperties asyncProperties;

    public SimpleAsyncUncaughtExceptionHandler(AsyncProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (log.isWarnEnabled()) {
            log.warn("线程池{}执行任务异常,方法名为:{},参数为:{}", asyncProperties.getThreadNamePrefix(), method.getName(), params);
        }
        throw new RuntimeException(ex);
    }
}
