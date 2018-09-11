package com.suixingpay.config.server.mapper;

import com.jarvis.cache.annotation.Cache;
import com.jarvis.cache.annotation.CacheDelete;
import com.jarvis.cache.annotation.CacheDeleteKey;
import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午11:01:22
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午11:01:22
 */
public interface ApplicationMapper {
    /**
     * 添加应用
     *
     * @param application
     * @return
     */
    @CacheDelete(@CacheDeleteKey("'application_'+#args[0].name"))
    int addApplication(ApplicationDO application);

    /**
     * 修改应用
     *
     * @param application
     * @return
     */
    @CacheDelete(@CacheDeleteKey("'application_'+#args[0].name"))
    int updateApplication(ApplicationDO application);

    int EXPIRE = 3600 * 24;

    /**
     * 根据名称获取应用信息
     *
     * @param name
     * @return
     */
    @Cache(key = "'application_'+#args[0]", expire = EXPIRE)
    ApplicationDO getByName(String name);

    /**
     * 通过名字列表查询，如果参数为空，则查询所有
     *
     * @param names
     * @return
     */
    List<ApplicationDO> list(@Param("names") List<String> names);

    /**
     * 获取条件查询的记录数
     *
     * @param condition
     * @return
     */
    Long countByCondition(ApplicationCondition condition);

    /**
     * 获取条件查询的记录
     *
     * @param condition
     * @return
     */
    List<ApplicationDO> listByCondition(ApplicationCondition condition);

    /**
     * 查询用户的应用管理权限
     *
     * @param userId
     * @param profile
     * @param searchKey
     * @param applicationName
     * @return
     */
    List<ApplicationDO> listApplicationConfigRoleByUserIdAndProfile(@Param("userId") Integer userId,
                                                                    @Param("profile") String profile, @Param("searchKey") String searchKey,
                                                                    @Param("applicationName") String applicationName);
}
