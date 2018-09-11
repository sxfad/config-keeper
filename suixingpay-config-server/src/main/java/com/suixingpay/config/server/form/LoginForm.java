package com.suixingpay.config.server.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 登录表单
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月13日 上午11:02:57
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月13日 上午11:02:57
 */
@Data
@ApiModel
public class LoginForm {
    @ApiModelProperty(value = "登录名", required = true)
    @NotBlank(message = "登录名不能为空")
    private String username;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "登录密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码Key", required = true)
    @NotBlank(message = "验证码Key不能为空")
    private String key;

    @ApiModelProperty(value = "验证码", required = true)
    @NotBlank(message = "验证码不能为空")
    private String code;
}
