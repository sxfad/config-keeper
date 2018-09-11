package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.entity.UserApplicationConfigRoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午3:38:43
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午3:38:43
 */
public interface UserApplicationConfigRoleMapper {
    /**
     * 获取用户权限
     *
     * @param userId
     * @return
     */
    List<UserApplicationConfigRoleDO> listByUserId(Integer userId);

    /**
     * 添加用户权限
     *
     * @param role
     * @return
     */
    int addRole(UserApplicationConfigRoleDO role);

    /**
     * 批量增加
     *
     * @param roles
     * @return
     */
    int addRoles(@Param("roles") List<UserApplicationConfigRoleDO> roles);

    /**
     * 删除用户权限
     *
     * @param userId
     * @return
     */
    int deleteRolesByUserId(Integer userId);

    /**
     * 通过复合主键取值
     *
     * @param role
     * @return
     */
    UserApplicationConfigRoleDO getByKey(UserApplicationConfigRoleDO role);

    /**
     * 根据用户ID查询，其可管理的profiles
     *
     * @param userId
     * @return
     */
    List<String> listProfileByUserId(Integer userId);

}
