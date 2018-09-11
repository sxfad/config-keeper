package com.suixingpay.config.server.service.impl;

import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.mapper.UserMapper;
import com.suixingpay.config.server.service.UserService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:09:10
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:09:10
 */
@Service
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Long countByCondition(@NonNull UserCondition condition) {
        return userMapper.countByCondition(condition);
    }

    @Override
    public List<UserDO> listByCondition(@NonNull UserCondition condition) {
        return this.userMapper.listByCondition(condition);
    }

    @Override
    public UserDO getById(@NonNull Integer id) {
        return userMapper.getById(id);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void addUser(@NonNull UserDO user) {
        if (this.userMapper.addUser(user) <= 0) {
            throw new RuntimeException("add user fail");
        }
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void updateUser(@NonNull UserDO user) {
        if (this.userMapper.updateUser(user) <= 0) {
            throw new RuntimeException("Update use fail!");
        }

    }

    @Override
    public UserDO getByName(@NonNull String name) {
        return this.userMapper.getByName(name);
    }

}
