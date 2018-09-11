package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.*;
import com.suixingpay.config.server.exception.ForbiddenException;
import com.suixingpay.config.server.mapper.ApplicationMapper;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.mapper.UserApplicationConfigRoleMapper;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午2:30:24
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午2:30:24
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class UserApplicationConfigRoleServiceImpl implements UserApplicationConfigRoleService {

    @Autowired
    private UserApplicationConfigRoleMapper userApplicationConfigRoleMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    private final static int DEFAULT_PAGE = 0;
    private final static int DEFAULT_SIZE = 20;

    /**
     * @param userId
     * @return
     * @see com.suixingpay.config.server.service.UserApplicationConfigRoleService#listByUserId(java.lang.Integer)
     */
    @Override
    public List<UserApplicationConfigRoleDO> listByUserId(@NonNull Integer userId) {
        List<UserApplicationConfigRoleDO> res = userApplicationConfigRoleMapper.listByUserId(userId);
        if (null != res) {
            for (UserApplicationConfigRoleDO roleDO : res) {
                ProfileDO profileDO = this.profileMapper.getByProfile(roleDO.getProfile().getProfile());
                if (null != profileDO) {
                    roleDO.setProfile(profileDO);
                }
                ApplicationDO applicationDO = applicationMapper.getByName(roleDO.getApplication().getName());
                if (null != applicationDO) {
                    roleDO.setApplication(applicationDO);
                }
            }
        }
        return res;
    }

    /**
     * 用户&应用&环境关系 修改权限
     *
     * @param userId
     * @param applicationProfileRoles
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void saveRoles(@NonNull Integer userId, @NonNull List<ApplicationProfileRoleDO> applicationProfileRoles) {
        if (this.userApplicationConfigRoleMapper.deleteRolesByUserId(userId) < 0) {
            throw new RuntimeException("delete user roles fail!");
        }
        if (null == applicationProfileRoles || applicationProfileRoles.isEmpty()) {
            return;
        }
        List<UserApplicationConfigRoleDO> roles = new ArrayList<>();
        ApplicationDO applicationDO;
        ProfileDO profileDO;
        Map<String, ProfileDO> profileCache = new HashMap<>();
        UserApplicationConfigRoleDO role;
        for (ApplicationProfileRoleDO roleDO : applicationProfileRoles) {
            profileDO = profileCache.get(roleDO.getProfile().getProfile());
            if (null == profileDO) {
                profileDO = profileMapper.getByProfile(roleDO.getProfile().getProfile());
                if (null == profileDO) {
                    throw new RuntimeException(String.format("环境：%s不存在！", roleDO.getProfile().getProfile()));
                }
                profileCache.put(roleDO.getProfile().getProfile(), profileDO);
            }
            applicationDO = applicationMapper.getByName(roleDO.getApplication().getName());
            if (null == applicationDO) {
                throw new RuntimeException(String.format("应用：%s不存在！", roleDO.getApplication().getName()));
            }
            role = new UserApplicationConfigRoleDO();
            role.setApplication(roleDO.getApplication()).setProfile(roleDO.getProfile()).setUserId(userId);
            roles.add(role);
        }
        userApplicationConfigRoleMapper.addRoles(roles);
    }

    @Override
    public void checkHasRole(@NonNull UserDO userDO, @NonNull String application, @NonNull String profile) {
        if (userDO.isSuper()) {
            return;
        }
        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile(profile);

        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName(application);

        UserApplicationConfigRoleDO role = new UserApplicationConfigRoleDO();
        role.setUserId(userDO.getId()).setProfile(profileDO).setApplication(applicationDO);
        UserApplicationConfigRoleDO res = userApplicationConfigRoleMapper.getByKey(role);
        if (null == res) {
            throw new ForbiddenException("没有权限");
        }
    }

    @Override
    public List<ProfileDO> getProfilesByUser(@NonNull UserDO userDO) {
        if (userDO.isSuper()) {
            return profileMapper.listAll();
        }
        List<String> profiles = userApplicationConfigRoleMapper.listProfileByUserId(userDO.getId());
        List<ProfileDO> res = new ArrayList<>();
        if (null != profiles) {
            for (String profile : profiles) {
                res.add(this.profileMapper.getByProfile(profile));
            }
        }
        return res;
    }

    @Override
    public List<ApplicationDO> getApplicationsByUserAndProfile(@NonNull UserDO userDO, @NonNull String profile,
                                                               String searchKey, String applicationName) {
        if (userDO.isSuper()) {
            ApplicationCondition condition = new ApplicationCondition();
            condition.setName(applicationName);
            condition.setSearchKey(searchKey);
            Pageable pageable = new PageRequest(DEFAULT_PAGE, DEFAULT_SIZE);
            condition.setPageable(pageable);
            return applicationMapper.listByCondition(condition);
        }

        return applicationMapper.listApplicationConfigRoleByUserIdAndProfile(userDO.getId(), profile, searchKey,
                applicationName);
    }

}
