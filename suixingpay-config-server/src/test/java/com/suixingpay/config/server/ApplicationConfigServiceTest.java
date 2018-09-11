package com.suixingpay.config.server;

import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.SourceType;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.service.ApplicationConfigService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ApplicationConfigServiceTest extends BaseServiceTest {
    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        ApplicationConfigDO applicationConfigDO = new ApplicationConfigDO();

        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile("dev");

        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName("config-demo");

        UserDO userDO = UserDO.builder().id(2).administrator(YesNo.NO).build();

        applicationConfigDO.setProfile(profileDO).setApplication(applicationDO)
                .setPropertySource("suixingpay.test:1234").setSourceType(SourceType.PROPERTIES).setMemo("test")
                .setVersion(0)// 添加
                .setUser(userDO);
        applicationConfigService.saveApplicationConfig(applicationConfigDO);

        ApplicationConfigCondition condition = new ApplicationConfigCondition();
        Pageable pageable = new PageRequest(1, 20);
        condition.setPageable(pageable);
        List<ApplicationConfigDO> list = applicationConfigService.pageByCondition(condition).getContent();

        if (null != list) {
            for (ApplicationConfigDO configDO : list) {
                System.out.println(configDO);

                applicationConfigService.saveApplicationConfig(configDO);
            }
        }
        System.out.println(applicationConfigService.getByApplicationNameAnddProfile("config-demo", "dev"));
    }
}
