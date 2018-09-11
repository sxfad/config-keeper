package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.mapper.GlobalConfigLogMapper;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.service.GlobalConfigLogService;
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
public class GlobalConfigLogServiceImpl implements GlobalConfigLogService {
    @Autowired
    private GlobalConfigLogMapper globalConfigLogMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Override
    public Long countByCondition(@NonNull GlobalConfigLogCondition condition) {
        return globalConfigLogMapper.countByCondition(condition);
    }

    @Override
    public List<GlobalConfigLogDO> listByCondition(@NonNull GlobalConfigLogCondition condition) {
        List<GlobalConfigLogDO> res = globalConfigLogMapper.listByCondition(condition);
        if (null != res) {
            for (GlobalConfigLogDO log : res) {
                setProps(log);
            }
        }
        return res;
    }

    @Override
    public GlobalConfigLogDO getById(@NonNull Long id) {
        GlobalConfigLogDO res = globalConfigLogMapper.getById(id);
        setProps(res);
        return res;
    }

    private void setProps(GlobalConfigLogDO log) {
        if (null == log) {
            return;
        }
        ProfileDO profileDO = this.profileMapper.getByProfile(log.getProfile().getProfile());
        if (null != profileDO) {
            log.setProfile(profileDO);
        }
    }

}
