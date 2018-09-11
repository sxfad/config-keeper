package com.suixingpay.config.server;

import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.service.ProfileService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 上午10:00:14
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 上午10:00:14
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfileServiceTest extends BaseServiceTest {

    @Autowired
    private ProfileService profileService;

    @Test
    @Transactional
    @Rollback(true)
    public void test1Add() throws Exception {
        ProfileDO profile = new ProfileDO();
        profile.setProfile("test1");
        profile.setName("测试环境");
        profileService.addProfile(profile);
        profile = profileService.getByProfile("test1");
        System.out.println("detail-->" + profile);

        profileService.updateProfile(profile);

        List<ProfileDO> list = profileService.listAll();
        Assert.assertNotNull(list);
        for (ProfileDO p : list) {
            System.out.println("list item --->" + p);
        }
    }
}
