package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午3:29:49
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午3:29:49
 */
public interface ApplicationConfigLogService
        extends PageableService<ApplicationConfigLogCondition, ApplicationConfigLogDO> {

    /**
     * TODO
     *
     * @param id
     * @return
     */
    ApplicationConfigLogDO getById(Long id);
}
