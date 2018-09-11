package com.suixingpay.config.server;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年12月12日 上午10:44:50
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年12月12日 上午10:44:50
 */
public class MailTest extends BaseServiceTest {

    @Autowired
    private MailUtil mailUtil;

    @Test
    public void testSendMail() throws Exception {
        mailUtil.sendMail("qiu_jy@suixingpay.com", "邮件测试", "<b>邮件</b>测试");
    }
}
