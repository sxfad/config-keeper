package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.entity.UserGlobalConfigRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午3:38:43
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午3:38:43
 */
public interface UserGlobalConfigRoleMapper {
    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    List<UserGlobalConfigRoleDO> listByUserId(Integer userId);

    /**
     * 添加用户权限
     *
     * @param roles
     * @return
     */
    int addRoles(@Param("roles") List<UserGlobalConfigRoleDO> roles);

    /**
     * 删除用户权限
     *
     * @param userId
     * @return
     */
    int deleteUserRoles(Integer userId);
}
