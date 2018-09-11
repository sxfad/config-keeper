package com.suixingpay.config.server.service.impl;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.service.ProfileService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:26:02
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:26:02
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void addProfile(@NonNull ProfileDO profile) {
        if (this.profileMapper.addProfile(profile) <= 0) {
            throw new RuntimeException("add profile fail");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void updateProfile(@NonNull ProfileDO profile) {
        if (this.profileMapper.updateProfile(profile) <= 0) {
            throw new RuntimeException("update profile fail");
        }
    }

    @Override
    public ProfileDO getByProfile(@NonNull String profile) {
        return this.profileMapper.getByProfile(profile);
    }

    @Override
    public List<ProfileDO> listAll() {
        return this.profileMapper.listAll();
    }

}
