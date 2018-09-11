package com.suixingpay.config.server;

import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.Status;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.service.UserService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 上午11:30:43
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 上午11:30:43
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends BaseServiceTest {
    @Autowired
    private UserService userService;

    @Test
    @Transactional
    @Rollback(true)
    public void test1Add() throws Exception {
        UserDO userDO = UserDO.builder().name("tmp").administrator(YesNo.NO).email("t@s.com").password("aaaa")
                .status(Status.VALID).build();
        userService.addUser(userDO);
        userDO = userService.getByName("tmp");
        userService.updateUser(userDO);

        UserCondition condition = new UserCondition();
        Pageable pageable = new PageRequest(1, 10);
        condition.setPageable(pageable);

        Page<UserDO> page = userService.pageByCondition(condition);
        Assert.assertNotNull(page);
        for (UserDO p : page.getContent()) {
            System.out.println("list item --->" + p);
        }

        userDO = userService.getById(1);
        System.out.println("detail-->" + userDO);
    }

}
