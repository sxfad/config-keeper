package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午3:29:49
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午3:29:49
 */
public interface GlobalConfigLogService extends PageableService<GlobalConfigLogCondition, GlobalConfigLogDO> {

    /**
     * TODO
     *
     * @param id
     * @return
     */
    GlobalConfigLogDO getById(Long id);
}
