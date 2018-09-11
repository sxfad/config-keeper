package com.suixingpay.config.server;

import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.SourceType;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.service.GlobalConfigService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
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
public class GlobalConfigServiceTest extends BaseServiceTest {
    @Autowired
    private GlobalConfigService globalConfigService;

    @Test
    @Transactional
    @Rollback(true)
    public void test() {
        GlobalConfigDO globalConfigDO = new GlobalConfigDO();

        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile("dev");
        globalConfigDO.setProfile(profileDO);

        globalConfigDO.setPropertySource("suixingpay.test:1234");
        globalConfigDO.setSourceType(SourceType.PROPERTIES);
        globalConfigDO.setMemo("test");
        globalConfigDO.setVersion(0);// 添加
        UserDO userDO = new UserDO();
        userDO.setId(2);
        userDO.setAdministrator(YesNo.NO);
        globalConfigDO.setUser(userDO);
        globalConfigService.saveGlobalConfig(globalConfigDO);

        List<GlobalConfigDO> list = globalConfigService.listByUser(userDO);

        if (null != list) {
            for (GlobalConfigDO configDO : list) {
                System.out.println(configDO);

                globalConfigService.saveGlobalConfig(configDO);
            }
        }
        System.out.println(globalConfigService.getByProfile("dev"));
    }
}
