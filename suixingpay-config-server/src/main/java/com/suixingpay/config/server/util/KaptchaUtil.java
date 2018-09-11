package com.suixingpay.config.server.util;

import com.google.code.kaptcha.Producer;
import com.suixingpay.config.server.dto.Kaptcha;
import com.suixingpay.config.server.redis.IRedisOperater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.UUID;

/**
 * google Kaptcha 验证码生成工具类
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午5:46:20
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午5:46:20
 */
@Component
public class KaptchaUtil {

    private static final Integer EXPIRE = 120;

    private static final String IMG_SRC_PREFIX = "data:image/jpeg;base64,";
    @Autowired
    private Producer captchaProducer;

    @Autowired
    private IRedisOperater redisOperater;

    /**
     * 生成验证码，并放到Redis中
     *
     * @return
     * @throws Exception
     */
    public Kaptcha createImage() throws Exception {
        // 生成验证码文本
        String capText = captchaProducer.createText();
        // 利用生成的字符串构建图片
        BufferedImage bi = captchaProducer.createImage(capText);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", out);
        String key = UUID.randomUUID().toString().replaceAll("-", "");
        Kaptcha kaptcha = new Kaptcha();
        kaptcha.setKey(key);
        kaptcha.setBase64Code(IMG_SRC_PREFIX + Base64.getEncoder().encodeToString(out.toByteArray()));
        // 缓存到Redis中
        redisOperater.setex(key, capText, EXPIRE);
        return kaptcha;
    }

    /**
     * 检查验证码是否正确
     *
     * @param key
     * @param inputCode
     * @return
     */
    public boolean check(String key, String inputCode) {
        if (null == key || key.length() == 0 || null == inputCode || inputCode.length() == 0) {
            return false;
        }
        String code = (String) redisOperater.get(key);
        return inputCode.equalsIgnoreCase(code);
    }
}
