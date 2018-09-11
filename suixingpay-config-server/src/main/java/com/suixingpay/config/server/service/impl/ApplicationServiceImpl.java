package com.suixingpay.config.server.service.impl;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.mapper.ApplicationMapper;
import com.suixingpay.config.server.service.ApplicationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:29:57
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:29:57
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void addApplication(@NonNull ApplicationDO application) {
        if (this.applicationMapper.addApplication(application) <= 0) {
            throw new RuntimeException("add application fail");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void updateApplication(@NonNull ApplicationDO application) {
        if (this.applicationMapper.updateApplication(application) <= 0) {
            throw new RuntimeException("update application fail");
        }
    }

    @Override
    public ApplicationDO getByName(@NonNull String name) {
        return this.applicationMapper.getByName(name);
    }

    @Override
    public Long countByCondition(@NonNull ApplicationCondition condition) {
        return applicationMapper.countByCondition(condition);
    }

    @Override
    public List<ApplicationDO> listByCondition(@NonNull ApplicationCondition condition) {
        return applicationMapper.listByCondition(condition);
    }

}
