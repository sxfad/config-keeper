package com.suixingpay.config.server.service;

import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.UserDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午3:31:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午3:31:14
 */
public interface GlobalConfigService {

    /**
     * 获取全局配置
     *
     * @param profile
     * @return
     */
    GlobalConfigDO getByProfile(String profile);

    /**
     * 获取全局配置（不带Profile信息）
     *
     * @param profile
     * @return
     */
    GlobalConfigDO getByProfileForOpenApi(String profile);

    /**
     * 根据用户权限进行查询
     *
     * @param userDO
     * @return
     */
    List<GlobalConfigDO> listByUser(UserDO userDO);

    /**
     * TODO
     *
     * @param globalConfigDO
     */
    void saveGlobalConfig(GlobalConfigDO globalConfigDO);

    /**
     * 历史版本替换
     *
     * @param globalConfigDO
     */
    void replaceGlobalConfig(GlobalConfigDO globalConfigDO);

}
