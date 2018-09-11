package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.entity.UserDO;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:08:59
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:08:59
 */
public interface UserService extends PageableService<UserCondition, UserDO> {

    String HAS_SUPER_ROLE = "SUPER";

    String HAS_NORMAL_ROLE = "ROLE_NORMAL";

    /**
     * TODO
     *
     * @param id
     * @return
     */
    UserDO getById(Integer id);

    /**
     * TODO
     *
     * @param name
     * @return
     */
    UserDO getByName(String name);

    /**
     * TODO
     *
     * @param user
     */
    void addUser(UserDO user);

    /**
     * TODO
     *
     * @param user
     */
    void updateUser(UserDO user);

}
