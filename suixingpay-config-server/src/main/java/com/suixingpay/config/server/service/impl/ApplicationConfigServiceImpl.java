package com.suixingpay.config.server.service.impl;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.mapper.ApplicationConfigLogMapper;
import com.suixingpay.config.server.mapper.ApplicationConfigMapper;
import com.suixingpay.config.server.mapper.ApplicationMapper;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.service.ApplicationConfigService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午3:32:27
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午3:32:27
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class ApplicationConfigServiceImpl implements ApplicationConfigService {

    @Autowired
    private ApplicationConfigMapper applicationConfigMapper;

    @Autowired
    private ApplicationConfigLogMapper applicationConfigLogMapper;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void saveApplicationConfig(@NonNull ApplicationConfigDO applicationConfigDO) {
        String applicationName = applicationConfigDO.getApplication().getName();
        String profile = applicationConfigDO.getProfile().getProfile();
        ProfileDO profileDO = profileMapper.getByProfile(profile);
        if (profileDO == null) {
            throw new RuntimeException("环境不存在！");
        }
        ApplicationDO applicationDO = applicationMapper.getByName(applicationName);
        if (applicationDO == null) {
            throw new RuntimeException("应用不存在！");
        }

        ApplicationConfigDO tmp = applicationConfigMapper.getByApplicationNameAnddProfile(applicationName, profile);
        if (null == tmp) {
            applicationConfigMapper.addApplicationConfig(applicationConfigDO);
        } else {
            if (null == applicationConfigDO.getVersion()) {
                throw new RuntimeException("版本号不能为空！");
            }
            if (null == tmp.getVersion()) {
                throw new RuntimeException("数据库中版本号为空！");
            }
            if (applicationConfigDO.getVersion().intValue() != tmp.getVersion().intValue()) {
                applicationConfigMapper.removeCache(applicationConfigDO);
                throw new RuntimeException("数据版本不正确，请刷新数据后再重试！");
            }
            if (tmp.getPropertySource().equals(applicationConfigDO.getPropertySource())) {
                throw new RuntimeException("没有修改配置内容！");
            }
            if (applicationConfigMapper.updateApplicationConfig(applicationConfigDO) <= 0) {
                applicationConfigMapper.removeCache(applicationConfigDO);
                throw new RuntimeException("更新数据失败！");
            }
            ApplicationConfigLogDO applicationConfigLogDO = new ApplicationConfigLogDO();
            applicationConfigLogDO.setProfile(tmp.getProfile()).setApplication(tmp.getApplication())
                    .setPropertySource(tmp.getPropertySource()).setSourceType(tmp.getSourceType())
                    .setVersion(tmp.getVersion()).setMemo(tmp.getMemo()).setUser(tmp.getUser())
                    .setModifyTime(tmp.getModifyTime());
            applicationConfigLogMapper.addApplicationConfigLog(applicationConfigLogDO);
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    @CacheDeleteTransactional
    public void replaceApplicationConfig(@NonNull ApplicationConfigDO applicationConfigDO) {
        String applicationName = applicationConfigDO.getApplication().getName();
        String profile = applicationConfigDO.getProfile().getProfile();
        ProfileDO profileDO = profileMapper.getByProfile(profile);
        if (profileDO == null) {
            throw new RuntimeException("环境不存在！");
        }
        ApplicationDO applicationDO = applicationMapper.getByName(applicationName);
        if (applicationDO == null) {
            throw new RuntimeException("应用不存在！");
        }

        ApplicationConfigDO tmp = applicationConfigMapper.getByApplicationNameAnddProfile(applicationName, profile);
        if (null == tmp) {
            applicationConfigMapper.addApplicationConfig(applicationConfigDO);
        } else {
            if (tmp.getPropertySource().equals(applicationConfigDO.getPropertySource())) {
                throw new RuntimeException("没有修改配置内容！");
            }
            applicationConfigDO.setVersion(tmp.getVersion());
            if (applicationConfigMapper.updateApplicationConfig(applicationConfigDO) <= 0) {
                throw new RuntimeException("更新数据失败！");
            }
            ApplicationConfigLogDO applicationConfigLogDO = new ApplicationConfigLogDO();
            applicationConfigLogDO.setProfile(tmp.getProfile()).setApplication(tmp.getApplication())
                    .setPropertySource(tmp.getPropertySource()).setSourceType(tmp.getSourceType())
                    .setVersion(tmp.getVersion()).setMemo(tmp.getMemo()).setUser(tmp.getUser())
                    .setModifyTime(tmp.getModifyTime());
            applicationConfigLogMapper.addApplicationConfigLog(applicationConfigLogDO);
        }
    }

    @Override
    public Long countByCondition(@NonNull ApplicationConfigCondition condition) {
        return this.applicationConfigMapper.countByCondition(condition);
    }

    @Override
    public List<ApplicationConfigDO> listByCondition(@NonNull ApplicationConfigCondition condition) {
        List<ApplicationConfigDO> list = this.applicationConfigMapper.listByCondition(condition);
        if (null != list && list.size() > 0) {
            list.stream().forEach(log -> setProps(log));
        }
        return list;
    }

    @Override
    public ApplicationConfigDO getByApplicationNameAnddProfile(@NonNull String applicationName,
                                                               @NonNull String profile) {
        ProfileDO profileDO = profileMapper.getByProfile(profile);
        if (profileDO == null) {
            throw new RuntimeException("环境不存在！");
        }
        ApplicationDO applicationDO = applicationMapper.getByName(applicationName);
        if (applicationDO == null) {
            throw new RuntimeException("应用不存在！");
        }
        ApplicationConfigDO res = applicationConfigMapper.getByApplicationNameAnddProfile(applicationName, profile);
        setProps(res);
        return res;
    }

    @Override
    public ApplicationConfigDO getForOpenApi(@NonNull String applicationName, @NonNull String profile) {
        return applicationConfigMapper.getByApplicationNameAnddProfile(applicationName, profile);
    }

    private void setProps(ApplicationConfigDO res) {
        if (null != res) {
            ProfileDO profileDO = this.profileMapper.getByProfile(res.getProfile().getProfile());
            if (null != profileDO) {
                res.setProfile(profileDO);
            }
            ApplicationDO applicationDO = this.applicationMapper.getByName(res.getApplication().getName());
            if (null != applicationDO) {
                res.setApplication(applicationDO);
            }
        }
    }

}
