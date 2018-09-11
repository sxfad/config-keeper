package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:41:46
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:41:46
 */
public interface ApplicationConfigLogMapper {

    /**
     * 根据ID获取日志
     *
     * @param id
     * @return
     */
    ApplicationConfigLogDO getById(Long id);

    /**
     * 根据查询条件，获取记录数
     *
     * @param condition
     * @return
     */
    Long countByCondition(ApplicationConfigLogCondition condition);

    /**
     * 根据查询条件获取记录
     *
     * @param condition
     * @return
     */
    List<ApplicationConfigLogDO> listByCondition(ApplicationConfigLogCondition condition);

    /**
     * 添加日志
     *
     * @param globalConfigLogDO
     * @return
     */
    int addApplicationConfigLog(ApplicationConfigLogDO globalConfigLogDO);
}
