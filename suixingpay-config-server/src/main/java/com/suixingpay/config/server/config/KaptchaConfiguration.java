package com.suixingpay.config.server.config;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * google Kaptcha 验证码生成工具配置
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午4:13:47
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午4:13:47
 */
@Configuration
public class KaptchaConfiguration {

    @Bean
    public Producer captchaProducer() {
        Properties props = new Properties();
        // 是否有边框, 默认为true 可选yes 或者 no
        props.put("kaptcha.border", "no");
        // 边框颜色, 默认为Color.BLACK
        // props.put("kaptcha.border.color", "105,179,90");
        // kaptcha.border.thickness 边框粗细度 默认为1

        // 验证码图片的宽度 默认200
        props.put("kaptcha.image.width", "200");
        // 验证码图片的高度 默认50
        props.put("kaptcha.image.height", "50");
        // 验证码文本字符长度 默认为5
        props.put("kaptcha.textproducer.char.length", "5");
        /**
         * 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1,
         * fontSize)
         **/
        // props.put("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
        // 验证码文本字符大小 默认为40
        props.put("kaptcha.textproducer.font.size", "40");
        // 验证码文本字符颜色 默认为Color.BLACK
        props.put("kaptcha.textproducer.font.color", "blue");
        // 验证码文本字符内容范围 默认为abcde2345678gfynmnpwx
        props.put("kaptcha.textproducer.char.string", "aAbBcCdDeEfFgG2345678hHjJkKmMLnNpPrRsStTuUvVwWxXyY");

        // kaptcha.producer.impl 验证码生成器 默认为DefaultKaptcha
        // kaptcha.textproducer.impl 验证码文本生成器 默认为DefaultTextCreator
        // kaptcha.textproducer.char.space 验证码文本字符间距 默认为2
        // kaptcha.noise.impl 验证码噪点生成对象 默认为DefaultNoise
        // kaptcha.noise.color 验证码噪点颜色 默认为Color.BLACK
        // kaptcha.obscurificator.impl 验证码样式引擎 默认为WaterRipple
        // kaptcha.word.impl 验证码文本字符渲染 默认为DefaultWordRenderer
        // kaptcha.background.impl 验证码背景生成器 默认为DefaultBackground
        // kaptcha.background.clear.from 验证码背景颜色渐进 默认为Color.LIGHT_GRAY
        // kaptcha.background.clear.to 验证码背景颜色渐进 默认为Color.WHITE

        Config config = new Config(props);
        DefaultKaptcha captchaProducer = new DefaultKaptcha();
        captchaProducer.setConfig(config);
        return captchaProducer;
    }
}
