package com.suixingpay.cloud.test.web;

import com.suixingpay.cloud.test.dto.DefaultUserWapper;
import com.suixingpay.cloud.test.dto.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月6日 下午6:10:42
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月6日 下午6:10:42
 */
@RestController
// @RefreshScope  // 如果不加此，下面的@Value 无法刷新
public class ConfigTestController {

    @Autowired
    @Qualifier("defaultUser")
    private UserDO defaultUser;

    @Value("${configSwitch:false}")
    private boolean configSwitch = false;

    @Autowired
    private DefaultUserWapper defaultUserWapper;

    @GetMapping({"/", "index"})
    public String getConfig() {
        return defaultUser.toString() + "--" + configSwitch + ";defaultUserWapper-->" + defaultUserWapper;
    }
}
