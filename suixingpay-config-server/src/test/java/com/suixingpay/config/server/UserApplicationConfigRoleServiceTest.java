package com.suixingpay.config.server;

import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ApplicationProfileRoleDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserApplicationConfigRoleDO;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午2:36:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午2:36:34
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserApplicationConfigRoleServiceTest extends BaseServiceTest {

    @Autowired
    private UserApplicationConfigRoleService userApplicationConfigRoleService;

    @Test
    @Transactional
    @Rollback(true)
    public void test1() {
        List<ApplicationProfileRoleDO> applicationRoles = new ArrayList<ApplicationProfileRoleDO>();
        ProfileDO dev = new ProfileDO();
        dev.setProfile("dev");

        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName("config-demo");
        applicationRoles.add(new ApplicationProfileRoleDO().setProfile(dev).setApplication(applicationDO));

        ProfileDO test = new ProfileDO();
        test.setProfile("test");

        applicationRoles.add(new ApplicationProfileRoleDO().setProfile(test).setApplication(applicationDO));

        this.userApplicationConfigRoleService.saveRoles(2, applicationRoles);
        List<UserApplicationConfigRoleDO> list = this.userApplicationConfigRoleService.listByUserId(2);
        list.stream().forEach(item -> {
            System.out.println(item);
        });
        this.userApplicationConfigRoleService.saveRoles(2, applicationRoles);
    }

}
