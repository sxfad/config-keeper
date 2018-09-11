package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:41:46
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:41:46
 */
public interface GlobalConfigLogMapper {

    /**
     * TODO
     *
     * @param id
     * @return
     */
    GlobalConfigLogDO getById(Long id);

    /**
     * TODO
     *
     * @param condition
     * @return
     */
    Long countByCondition(GlobalConfigLogCondition condition);

    /**
     * TODO
     *
     * @param condition
     * @return
     */
    List<GlobalConfigLogDO> listByCondition(GlobalConfigLogCondition condition);

    /**
     * TODO
     *
     * @param globalConfigLogDO
     * @return
     */
    int addGlobalConfigLog(GlobalConfigLogDO globalConfigLogDO);
}
