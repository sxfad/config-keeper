package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationConfigDO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午3:31:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午3:31:14
 */
public interface ApplicationConfigService extends PageableService<ApplicationConfigCondition, ApplicationConfigDO> {

    /**
     * TODO
     *
     * @param applicationName
     * @param profile
     * @return
     */
    ApplicationConfigDO getByApplicationNameAnddProfile(String applicationName, String profile);

    /**
     * 不带Profile和Application信息
     *
     * @param applicationName
     * @param profile
     * @return
     */
    ApplicationConfigDO getForOpenApi(String applicationName, String profile);

    /**
     * TODO
     *
     * @param applicationConfigDO
     */
    void saveApplicationConfig(ApplicationConfigDO applicationConfigDO);

    /**
     * TODO
     *
     * @param applicationConfigDO
     */
    void replaceApplicationConfig(ApplicationConfigDO applicationConfigDO);
}
