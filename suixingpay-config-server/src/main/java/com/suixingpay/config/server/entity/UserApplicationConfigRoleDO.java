package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 应用程序配置权限
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午5:10:40
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午5:10:40
 */
@Data
@Accessors(chain = true)
@ApiModel
public class UserApplicationConfigRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("应用")
    private ApplicationDO application;
    @ApiModelProperty("profile")
    private ProfileDO profile;
}
