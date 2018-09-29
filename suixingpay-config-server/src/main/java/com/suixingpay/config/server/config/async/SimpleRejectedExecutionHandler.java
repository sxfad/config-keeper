package com.suixingpay.config.server.config.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 自定义拒绝策略
 * 
 * @author: wanghongfei[wang_hf@suixingpay.com]
 * @date: 2018年7月24日 上午10:35:09
 * @version: V1.0
 * @review: wanghongfei[wang_hf@suixingpay.com]/2018年7月24日 上午10:35:09
 */
@Slf4j
public class SimpleRejectedExecutionHandler implements RejectedExecutionHandler {

    private AsyncProperties asyncProperties;

    public SimpleRejectedExecutionHandler(AsyncProperties asyncProperties) {
        this.asyncProperties = asyncProperties;
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (log.isWarnEnabled()) {
            String msg = String.format(
                    "线程池资源已经耗尽,线程池名称:%s[线程池总线程数:%d,活跃线程数:%d,队列数量:%d,完成任务数量:%d]"
                            + "线程状态[isShutDown:%s, isTerminated:%s, isTerminating:%s]",
                    asyncProperties.getThreadNamePrefix(), e.getPoolSize(), e.getActiveCount(), e.getQueue().size(),
                    e.getCompletedTaskCount(), e.isShutdown(), e.isTerminated(), e.isTerminating());
            log.warn(msg);
        }

        throw new RejectedExecutionException("Task " + r.toString() + " rejected from " + e.toString());
    }

}
