package com.suixingpay.config.server.config.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;

/**
 * 异步调用线程池默认调用配置
 *
 * @author: wanghongfei[wang_hf@suixingpay.com]
 * @date: 2018年7月24日 上午10:35:09
 * @version: V1.0
 * @review: wanghongfei[wang_hf@suixingpay.com]/2018年7月24日 上午10:35:09
 */
@Configuration
@EnableAsync
@EnableConfigurationProperties(AsyncProperties.class)
public class AsyncAutoConfiguration implements AsyncConfigurer, BeanFactoryAware {

    private BeanFactory beanFactory;

    @Autowired
    private AsyncProperties asyncProperties;

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(asyncProperties.getCorePoolSize());
        executor.setMaxPoolSize(asyncProperties.getMaxPoolSize());
        executor.setQueueCapacity(asyncProperties.getQueueCapacity());
        executor.setKeepAliveSeconds(asyncProperties.getKeepAliveSeconds());
        executor.setThreadNamePrefix(asyncProperties.getThreadNamePrefix());
        executor.setRejectedExecutionHandler(getRejectedExecutionHandler());
        executor.setDaemon(asyncProperties.isDaemon());
        executor.initialize();
        return executor;
    }

    private RejectedExecutionHandler getRejectedExecutionHandler() {
        String rejectHandlerName = asyncProperties.getRejectedExecutionHandlerBeanName();
        if (null != rejectHandlerName && !rejectHandlerName.isEmpty()) {
            if (beanFactory.containsBean(rejectHandlerName)) {
                Object object = beanFactory.getBean(rejectHandlerName);
                if (null != object && object instanceof RejectedExecutionHandler) {
                    return (RejectedExecutionHandler) object;
                }
            }
        }
        return new SimpleRejectedExecutionHandler(asyncProperties);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        String uncaughtHandlerName = asyncProperties.getAsyncUncaughtExceptionHandlerBeanName();
        if (null != uncaughtHandlerName && !uncaughtHandlerName.isEmpty()) {
            if (beanFactory.containsBean(uncaughtHandlerName)) {
                Object object = beanFactory.getBean(uncaughtHandlerName);
                if (null != object && object instanceof AsyncUncaughtExceptionHandler) {
                    return (AsyncUncaughtExceptionHandler) object;
                }
            }
        }
        return new SimpleAsyncUncaughtExceptionHandler(asyncProperties);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
