package com.suixingpay.config.server.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月14日 下午5:52:15
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月14日 下午5:52:15
 */
@Getter
@Setter
@ApiModel
public class Kaptcha {
    /**
     * Redis缓存验证码的key
     */
    @ApiModelProperty(value = "验证码的key")
    private String key;
    /**
     * 使用Base64编码的验证码图片
     */
    @ApiModelProperty(value = "Base64编码的验证码图片")
    private String base64Code;
}
