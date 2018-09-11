package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.entity.UserDO;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午5:08:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午5:08:32
 */
public interface UserMapper {

    /**
     * 根据id获取用户信息
     *
     * @param id
     * @return
     */
    UserDO getById(Integer id);

    /**
     * 添加用户信息
     *
     * @param user
     * @return
     */
    int addUser(UserDO user);

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    int updateUser(UserDO user);

    /**
     * 获取条件查询记录数
     *
     * @param condition
     * @return
     */
    Long countByCondition(UserCondition condition);

    /**
     * 条件查询
     *
     * @param condition
     * @return
     */
    List<UserDO> listByCondition(UserCondition condition);

    /**
     * 根据用户名获取用户信息
     *
     * @param name
     * @return
     */
    UserDO getByName(String name);
}
