package com.suixingpay.config.server.mapper;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.annotation.CacheDeleteKey;
import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.UserDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:41:46
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:41:46
 */
public interface GlobalConfigMapper {

    /**
     * TODO
     *
     * @param profile
     * @return
     */
    @Cache(expire = 20 * 60, key = "'global_config:'+#args[0]")
    GlobalConfigDO getByProfile(String profile);

    /**
     * 根据用户权限查询
     *
     * @param user
     * @return
     */
    List<GlobalConfigDO> listByUser(UserDO user);

    /**
     * TODO
     *
     * @param globalConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'global_config:'+#args[0].profile.profile") //
    })
    int addGlobalConfig(GlobalConfigDO globalConfigDO);

    /**
     * TODO
     *
     * @param globalConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'global_config:'+#args[0].profile.profile") //
    })
    int updateGlobalConfig(GlobalConfigDO globalConfigDO);

    /**
     * 清除缓存
     *
     * @param globalConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'global_config:'+#args[0].profile.profile") //
    })
    default void removeCache(GlobalConfigDO globalConfigDO) {

    }
}
