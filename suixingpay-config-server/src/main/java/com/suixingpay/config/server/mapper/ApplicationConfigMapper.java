package com.suixingpay.config.server.mapper;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.annotation.CacheDeleteKey;
import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:41:46
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:41:46
 */
public interface ApplicationConfigMapper {

    /**
     * 获取应用配置
     *
     * @param applicationName
     * @param profile
     * @return
     */
    @Cache(expire = 20 * 60, key = "'application_config:'+#args[0]+':'+#args[1]")
    ApplicationConfigDO getByApplicationNameAnddProfile(@Param("applicationName") String applicationName,
                                                        @Param("profile") String profile);

    /**
     * 获取条件查询记录数
     *
     * @param condition
     * @return
     */
    Long countByCondition(ApplicationConfigCondition condition);

    /**
     * 根据查询获取记录
     *
     * @param condition
     * @return
     */
    List<ApplicationConfigDO> listByCondition(ApplicationConfigCondition condition);

    /**
     * 添加配置
     *
     * @param applicationConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'application_config:'+#args[0].application.name+':'+#args[0].profile.profile") //
    })
    int addApplicationConfig(ApplicationConfigDO applicationConfigDO);

    /**
     * 修改配置
     *
     * @param applicationConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'application_config:'+#args[0].application.name+':'+#args[0].profile.profile") //
    })
    int updateApplicationConfig(ApplicationConfigDO applicationConfigDO);

    /**
     * 修改配置
     *
     * @param applicationConfigDO
     * @return
     */
    @CacheDelete({ //
            @CacheDeleteKey("'application_config:'+#args[0].application.name+':'+#args[0].profile.profile") //
    })
    default void removeCache(ApplicationConfigDO applicationConfigDO) {

    }
}
