package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.ApplicationDO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:23:49
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:23:49
 */
public interface ApplicationService extends PageableService<ApplicationCondition, ApplicationDO> {
    /**
     * 添加应用
     *
     * @param application
     * @return
     */
    void addApplication(ApplicationDO application);

    /**
     * 修改应用
     *
     * @param application
     * @return
     */
    void updateApplication(ApplicationDO application);

    /**
     * 根据名称获取应用
     *
     * @param name
     * @return
     */
    ApplicationDO getByName(String name);

}
