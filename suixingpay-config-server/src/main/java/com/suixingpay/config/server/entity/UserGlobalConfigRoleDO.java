package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 全局配置权限
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午5:14:23
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午5:14:23
 */
@Data
@Accessors(chain = true)
@ApiModel
public class UserGlobalConfigRoleDO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("用户ID")
    private Integer userId;
    @ApiModelProperty("profile")
    private ProfileDO profile;
}
