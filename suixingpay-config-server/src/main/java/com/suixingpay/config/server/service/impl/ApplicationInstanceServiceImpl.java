package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.condition.ApplicationInstanceCondition;
import com.suixingpay.config.server.entity.ApplicationInstanceDO;
import com.suixingpay.config.server.mapper.ApplicationInstanceMapper;
import com.suixingpay.config.server.service.ApplicationInstanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 14时34分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 14时34分
 */
@Slf4j
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class ApplicationInstanceServiceImpl implements ApplicationInstanceService {

    @Autowired
    private ApplicationInstanceMapper instanceMapper;

    @Async
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(ApplicationInstanceDO instanceDO) {
        if (log.isDebugEnabled()) {
            log.debug("开启Async存储实例");
        }
        if (null != instanceDO && null != instanceDO.getApplicationName() && null != instanceDO.getProfile() && null != instanceDO.getIp() && null != instanceDO.getPort()) {
            ApplicationInstanceDO one = instanceMapper.findOne(instanceDO);
            if (null == one) {
                instanceMapper.addInstance(instanceDO);
            } else if (!instanceDO.getManagerPath().equals(one.getManagerPath())) {
                instanceMapper.update(instanceDO);
            }
        }
    }

    @Override
    public List<ApplicationInstanceDO> list(ApplicationInstanceCondition condition) {
        return instanceMapper.list(condition);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void update(ApplicationInstanceDO applicationInstanceDO) {
        instanceMapper.update(applicationInstanceDO);
    }

    @Override
    public ApplicationInstanceDO findById(Long id) {
        return instanceMapper.findById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        instanceMapper.delete(id);
    }
}
