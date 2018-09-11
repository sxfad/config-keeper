package com.suixingpay.cloud.test.service;

import com.suixingpay.cloud.test.dto.UserDO;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月7日 下午5:10:13
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月7日 下午5:10:13
 */
public interface UserService {
    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    UserDO getUserById(Integer id);
}
