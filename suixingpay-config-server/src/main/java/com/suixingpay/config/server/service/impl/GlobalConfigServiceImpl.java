package com.suixingpay.config.server.service.impl;

import com.jarvis.cache.annotation.CacheDeleteTransactional;
import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.mapper.GlobalConfigLogMapper;
import com.suixingpay.config.server.mapper.GlobalConfigMapper;
import com.suixingpay.config.server.mapper.ProfileMapper;
import com.suixingpay.config.server.service.GlobalConfigService;
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
public class GlobalConfigServiceImpl implements GlobalConfigService {

    @Autowired
    private GlobalConfigMapper globalConfigMapper;

    @Autowired
    private GlobalConfigLogMapper globalConfigLogMapper;

    @Autowired
    private ProfileMapper profileMapper;

    private void setProps(GlobalConfigDO res) {
        if (null != res) {
            ProfileDO profileDO = this.profileMapper.getByProfile(res.getProfile().getProfile());
            if (null != profileDO) {
                res.setProfile(profileDO);
            }
        }
    }

    @Override
    public GlobalConfigDO getByProfile(@NonNull String profile) {
        // CacheHelper.setCacheOpType(CacheOpType.LOAD);
        GlobalConfigDO res = globalConfigMapper.getByProfile(profile);
        // CacheHelper.clearCacheOpType();
        setProps(res);
        return res;
    }

    @Override
    public GlobalConfigDO getByProfileForOpenApi(@NonNull String profile) {
        return globalConfigMapper.getByProfile(profile);
    }

    @Override
    public List<GlobalConfigDO> listByUser(@NonNull UserDO userDO) {
        List<GlobalConfigDO> res = globalConfigMapper.listByUser(userDO);
        if (null != res) {
            for (GlobalConfigDO globalConfigDO : res) {
                setProps(globalConfigDO);
            }
        }
        return res;
    }

    @Override
    @CacheDeleteTransactional
    @Transactional(rollbackFor = Throwable.class)
    public void saveGlobalConfig(@NonNull GlobalConfigDO globalConfigDO) {
        String profile = globalConfigDO.getProfile().getProfile();
        ProfileDO profileDO = profileMapper.getByProfile(profile);
        if (profileDO == null) {
            throw new RuntimeException("环境不存在！");
        }
        GlobalConfigDO tmp = globalConfigMapper.getByProfile(profile);
        if (null == tmp) {
            globalConfigMapper.addGlobalConfig(globalConfigDO);
        } else {
            if (null == globalConfigDO.getVersion()) {
                throw new RuntimeException("版本号不能为空！");
            }
            if (null == tmp.getVersion()) {
                throw new RuntimeException("数据库中版本号为空！");
            }
            if (globalConfigDO.getVersion().intValue() != tmp.getVersion().intValue()) {
                globalConfigMapper.removeCache(globalConfigDO);
                throw new RuntimeException("数据版本不正确，请刷新数据后再重试！");
            }
            if (tmp.getPropertySource().equals(globalConfigDO.getPropertySource())) {
                throw new RuntimeException("没有修改配置内容！");
            }
            if (globalConfigMapper.updateGlobalConfig(globalConfigDO) <= 0) {
                globalConfigMapper.removeCache(globalConfigDO);
                throw new RuntimeException("更新数据失败！");
            }
            GlobalConfigLogDO globalConfigLogDO = new GlobalConfigLogDO();
            globalConfigLogDO.setProfile(tmp.getProfile()).setPropertySource(tmp.getPropertySource())
                    .setSourceType(tmp.getSourceType()).setVersion(tmp.getVersion()).setMemo(tmp.getMemo())
                    .setUser(tmp.getUser()).setModifyTime(tmp.getModifyTime());
            globalConfigLogMapper.addGlobalConfigLog(globalConfigLogDO);
        }
    }

    @Override
    @CacheDeleteTransactional
    @Transactional(rollbackFor = Throwable.class)
    public void replaceGlobalConfig(@NonNull GlobalConfigDO globalConfigDO) {
        String profile = globalConfigDO.getProfile().getProfile();
        ProfileDO profileDO = profileMapper.getByProfile(profile);
        if (profileDO == null) {
            throw new RuntimeException("环境不存在！");
        }
        GlobalConfigDO tmp = globalConfigMapper.getByProfile(profile);
        if (null == tmp) {
            globalConfigMapper.addGlobalConfig(globalConfigDO);
        } else {
            if (tmp.getPropertySource().equals(globalConfigDO.getPropertySource())) {
                throw new RuntimeException("没有修改配置内容！");
            }
            // 设置为最新版本号
            globalConfigDO.setVersion(tmp.getVersion());
            if (globalConfigMapper.updateGlobalConfig(globalConfigDO) <= 0) {
                throw new RuntimeException("更新数据失败！");
            }
            GlobalConfigLogDO globalConfigLogDO = new GlobalConfigLogDO();
            globalConfigLogDO.setProfile(tmp.getProfile()).setPropertySource(tmp.getPropertySource())
                    .setSourceType(tmp.getSourceType()).setVersion(tmp.getVersion()).setMemo(tmp.getMemo())
                    .setUser(tmp.getUser()).setModifyTime(tmp.getModifyTime());
            globalConfigLogMapper.addGlobalConfigLog(globalConfigLogDO);
        }
    }
}
