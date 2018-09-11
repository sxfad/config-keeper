package com.suixingpay.config.server.service;

import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.entity.UserGlobalConfigRoleDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午1:37:41
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午1:37:41
 */
public interface UserGlobalConfigRoleService {
    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    List<UserGlobalConfigRoleDO> listByUserId(Integer userId);

    /**
     * 保存用户权限
     *
     * @param userId
     * @param profiles
     */
    void saveRoles(Integer userId, List<ProfileDO> profiles);

    /**
     * 判断用户是否有权限
     *
     * @param userDO
     * @param profile
     */
    void checkHasRole(UserDO userDO, String profile);

    /**
     * 获取当前用户可管理的Profiles
     *
     * @param userDO
     * @return
     */
    List<ProfileDO> getProfilesByUser(UserDO userDO);
}
