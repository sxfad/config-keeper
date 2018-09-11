package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.entity.UserGlobalConfigRoleDO;
import com.suixingpay.config.server.exception.ForbiddenException;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.mapper.UserGlobalConfigRoleMapper;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午1:40:26
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午1:40:26
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class UserGlobalConfigRoleServiceImpl implements UserGlobalConfigRoleService {

    @Autowired
    private UserGlobalConfigRoleMapper userGlobalConfigRoleMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public List<UserGlobalConfigRoleDO> listByUserId(@NonNull Integer userId) {
        List<UserGlobalConfigRoleDO> res = userGlobalConfigRoleMapper.listByUserId(userId);
        if (null != res) {
            for (UserGlobalConfigRoleDO roleDO : res) {
                ProfileDO profileDO = profileMapper.getByProfile(roleDO.getProfile().getProfile());
                if (null != profileDO) {
                    roleDO.setProfile(profileDO);
                }
            }
        }
        return res;
    }

    /**
     * 用户全局配置关系修改权限
     *
     * @param userId
     * @param profileRoles
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveRoles(@NonNull Integer userId, List<ProfileDO> profileRoles) {
        if (this.userGlobalConfigRoleMapper.deleteUserRoles(userId) < 0) {
            throw new RuntimeException("更新权限出错!");
        }
        if (null == profileRoles || profileRoles.isEmpty()) {
            return;
        }
        List<UserGlobalConfigRoleDO> roles = new ArrayList<>();
        UserGlobalConfigRoleDO roleDO;
        ProfileDO tmp;
        for (ProfileDO profile : profileRoles) {
            tmp = profileMapper.getByProfile(profile.getProfile());
            if (null == tmp) {
                throw new RuntimeException(String.format("环境：%s不存在！", profile.getProfile()));
            }
            roleDO = new UserGlobalConfigRoleDO();
            roleDO.setUserId(userId).setProfile(profile);
            roles.add(roleDO);
        }
        if (this.userGlobalConfigRoleMapper.addRoles(roles) <= 0) {
            throw new RuntimeException("更新权限出错");
        }
    }

    @Override
    public void checkHasRole(@NonNull UserDO userDO, @NonNull String profile) {
        if (userDO.isSuper()) {
            return;
        }
        List<UserGlobalConfigRoleDO> res = userGlobalConfigRoleMapper.listByUserId(userDO.getId());
        if (null != res) {
            for (UserGlobalConfigRoleDO roleDO : res) {
                if (roleDO.getProfile().getProfile().equals(profile)) {
                    return;
                }
            }
        }
        throw new ForbiddenException("没有权限");
    }

    @Override
    public List<ProfileDO> getProfilesByUser(@NonNull UserDO userDO) {
        if (userDO.isSuper()) {
            return profileMapper.listAll();
        }
        List<UserGlobalConfigRoleDO> roles = userGlobalConfigRoleMapper.listByUserId(userDO.getId());
        List<ProfileDO> res = new ArrayList<>();
        if (null != roles) {
            for (UserGlobalConfigRoleDO roleDO : roles) {
                res.add(this.profileMapper.getByProfile(roleDO.getProfile().getProfile()));
            }
        }
        return res;
    }

}
