package com.suixingpay.config.server;

import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.service.GlobalConfigLogService;
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
public class GlobalConfigLogServiceTest extends BaseServiceTest {
    @Autowired
    private GlobalConfigLogService globalConfigLogService;

    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        GlobalConfigLogCondition condition = new GlobalConfigLogCondition();
        Pageable pageable = new PageRequest(1, 10);
        condition.setPageable(pageable);
        UserDO userDO = new UserDO();
        userDO.setId(2);
        userDO.setAdministrator(YesNo.NO);

        condition.setUser(userDO);
        Page<GlobalConfigLogDO> page = globalConfigLogService.pageByCondition(condition);
        if (null != page) {
            List<GlobalConfigLogDO> list = page.getContent();
            for (GlobalConfigLogDO log : list) {
                System.out.println(log);
            }
        }

    }
}
