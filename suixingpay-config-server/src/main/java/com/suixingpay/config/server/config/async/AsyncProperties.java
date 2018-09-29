package com.suixingpay.config.server.config.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 线程池属性
 * 
 * @author: wanghongfei[wang_hf@suixingpay.com]
 * @date: 2018年7月24日 上午10:35:09
 * @version: V1.0
 * @review: wanghongfei[wang_hf@suixingpay.com]/2018年7月24日 上午10:35:09
 */
@Data
@ConfigurationProperties(prefix = AsyncProperties.PREFIX)
public class AsyncProperties {

    public static final String PREFIX = "suixingpay.async";

    /**
     * 线程名称前缀
     */
    private String threadNamePrefix = "AsyncExecutor-";

    /** 是否守护线程 **/
    private boolean daemon = false;

    /** 核心线程数 **/
    private int corePoolSize = Runtime.getRuntime().availableProcessors();

    /** 最大线程数 **/
    private int maxPoolSize = corePoolSize * 2;

    /** 线程池维护线程所允许的空闲时间 **/
    private int keepAliveSeconds = 60;

    /** 队列长度 **/
    private int queueCapacity = 1024;

    /** 线程池的拒绝策略 **/
    private String rejectedExecutionHandlerBeanName;

    /** 线程池任务执行异常处理策略 **/
    private String asyncUncaughtExceptionHandlerBeanName;

}
