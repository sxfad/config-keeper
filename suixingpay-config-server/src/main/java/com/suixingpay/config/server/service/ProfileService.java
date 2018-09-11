package com.suixingpay.config.server.service;

import com.suixingpay.config.server.entity.ProfileDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:25:03
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:25:03
 */
public interface ProfileService {
    /**
     * TODO
     *
     * @param profile
     */
    void addProfile(ProfileDO profile);

    /**
     * TODO
     *
     * @param profile
     */
    void updateProfile(ProfileDO profile);

    /**
     * TODO
     *
     * @param profile
     * @return
     */
    ProfileDO getByProfile(String profile);

    /**
     * TODO
     *
     * @return
     */
    List<ProfileDO> listAll();
}
