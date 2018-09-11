package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.mapper.ApplicationConfigLogMapper;
import com.suixingpay.config.server.mapper.ApplicationMapper;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.service.ApplicationConfigLogService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午3:32:57
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午3:32:57
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class ApplicationConfigLogServiceImpl implements ApplicationConfigLogService {
    @Autowired
    private ApplicationConfigLogMapper globalConfigLogMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public Long countByCondition(@NonNull ApplicationConfigLogCondition condition) {
        return globalConfigLogMapper.countByCondition(condition);
    }

    @Override
    public List<ApplicationConfigLogDO> listByCondition(@NonNull ApplicationConfigLogCondition condition) {
        List<ApplicationConfigLogDO> res = globalConfigLogMapper.listByCondition(condition);
        if (null != res) {
            for (ApplicationConfigLogDO log : res) {
                setProps(log);
            }
        }
        return res;
    }

    /**
     * @param id
     * @return
     * @see com.suixingpay.config.server.service.ApplicationConfigLogService#getById(java.lang.Long)
     */
    @Override
    public ApplicationConfigLogDO getById(@NonNull Long id) {
        ApplicationConfigLogDO log = globalConfigLogMapper.getById(id);
        setProps(log);
        return log;
    }

    private void setProps(ApplicationConfigLogDO log) {
        if (null == log) {
            return;
        }
        ProfileDO profileDO = this.profileMapper.getByProfile(log.getProfile().getProfile());
        if (null != profileDO) {
            log.setProfile(profileDO);
        }
        ApplicationDO applicationDO = applicationMapper.getByName(log.getApplication().getName());
        if (null != applicationDO) {
            log.setApplication(applicationDO);
        }
    }
}
