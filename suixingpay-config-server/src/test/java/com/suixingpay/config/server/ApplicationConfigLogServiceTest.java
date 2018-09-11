package com.suixingpay.config.server;

import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.service.ApplicationConfigLogService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月5日 下午3:53:38
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月5日 下午3:53:38
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApplicationConfigLogServiceTest extends BaseServiceTest {
    @Autowired
    private ApplicationConfigLogService globalConfigLogService;

    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        ApplicationConfigLogCondition condition = new ApplicationConfigLogCondition();
        Pageable pageable = new PageRequest(1, 10);
        condition.setPageable(pageable);
        UserDO userDO = new UserDO();
        userDO.setId(2);
        userDO.setAdministrator(YesNo.NO);

        condition.setUser(userDO);
        Page<ApplicationConfigLogDO> page = globalConfigLogService.pageByCondition(condition);
        if (null != page) {
            List<ApplicationConfigLogDO> list = page.getContent();
            for (ApplicationConfigLogDO log : list) {
                System.out.println(log);
            }
        }

    }
}
