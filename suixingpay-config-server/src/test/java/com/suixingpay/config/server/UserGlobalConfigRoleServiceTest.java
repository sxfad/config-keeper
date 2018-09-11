package com.suixingpay.config.server;

import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserGlobalConfigRoleDO;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午1:52:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午1:52:55
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserGlobalConfigRoleServiceTest extends BaseServiceTest {
    @Autowired
    private UserGlobalConfigRoleService userGlobalConfigRoleService;

    @Test
    @Transactional
    @Rollback(true)
    public void test1Add() {
        Integer userId = 2;
        List<ProfileDO> profiles = new ArrayList<>();
        profiles.add(new ProfileDO().setProfile("p1"));
        profiles.add(new ProfileDO().setProfile("p2"));
        this.userGlobalConfigRoleService.saveRoles(userId, profiles);

        List<UserGlobalConfigRoleDO> list = this.userGlobalConfigRoleService.listByUserId(userId);
        if (null != list) {
            list.stream().forEach(item -> {
                System.out.println(item);
            });
        }
    }

}
