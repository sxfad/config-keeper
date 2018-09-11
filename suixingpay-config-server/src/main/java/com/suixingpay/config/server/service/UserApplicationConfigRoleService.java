package com.suixingpay.config.server.service;

import com.suixingpay.config.server.entity.*;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午2:17:25
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午2:17:25
 */
public interface UserApplicationConfigRoleService {
    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    List<UserApplicationConfigRoleDO> listByUserId(Integer userId);

    /**
     * 保存权限
     *
     * @param userId
     * @param applicationRoles
     */
    void saveRoles(Integer userId, List<ApplicationProfileRoleDO> applicationRoles);

    /**
     * 判断用户是否有权限
     *
     * @param application
     * @param userDO
     * @param profile
     */
    void checkHasRole(UserDO userDO, String application, String profile);

    /**
     * 获取当前用户可管理的Profiles
     *
     * @param userDO
     * @return
     */
    List<ProfileDO> getProfilesByUser(UserDO userDO);

    /**
     * 获取当前用户可管理的Applications
     *
     * @param userDO
     * @param profile
     * @param searchKey
     * @param applicationName
     * @return
     */
    List<ApplicationDO> getApplicationsByUserAndProfile(UserDO userDO, String profile, String searchKey,
                                                        String applicationName);
}
