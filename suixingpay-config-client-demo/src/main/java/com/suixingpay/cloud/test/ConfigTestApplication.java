package com.suixingpay.cloud.test;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

/**
 * 测试应用
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 下午5:08:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 下午5:08:52
 */
@SpringBootApplication
public class ConfigTestApplication {
    protected final static Logger logger = LoggerFactory.getLogger(ConfigTestApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = configureApplication(new SpringApplicationBuilder()).run(args);
        Environment env = context.getBean(Environment.class);
        String[] profiles = env.getActiveProfiles();
        for (String tmp : profiles) {
            logger.info("active profile--->" + tmp);
        }
        logger.info("file=" + env.getProperty("file"));
        logger.info("file2==" + env.getProperty("file2"));
        logger.info("dataApplication is sussess!");
        // org.springframework.cloud.endpoint.GenericPostableMvcEndpoint tt;
        // org.springframework.cloud.endpoint.RefreshEndpoint d;
        // PropertySourceBootstrapConfiguration cc;

        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
        return builder.sources(ConfigTestApplication.class).bannerMode(Banner.Mode.OFF);
    }
}
