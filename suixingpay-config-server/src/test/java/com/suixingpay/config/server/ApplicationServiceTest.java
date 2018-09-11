package com.suixingpay.config.server;

import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.service.ApplicationService;
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
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 上午11:17:39
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 上午11:17:39
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationServiceTest extends BaseServiceTest {
    @Autowired
    private ApplicationService applicationService;

    @Test
    @Transactional
    @Rollback(true)
    public void test1Add() throws Exception {
        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName("config-test1");
        applicationDO.setDescription("配置测试1");
        applicationService.addApplication(applicationDO);

        applicationDO = applicationService.getByName("config-test1");
        System.out.println("detail-->" + applicationDO);
        Assert.assertNotNull(applicationDO);
        applicationService.updateApplication(applicationDO);

        ApplicationCondition condition = new ApplicationCondition();
        Pageable pageable = new PageRequest(1, 10);
        condition.setPageable(pageable);

        Page<ApplicationDO> page = applicationService.pageByCondition(condition);
        Assert.assertNotNull(page);
        for (ApplicationDO p : page.getContent()) {
            System.out.println("list item --->" + p);
        }
    }
}
