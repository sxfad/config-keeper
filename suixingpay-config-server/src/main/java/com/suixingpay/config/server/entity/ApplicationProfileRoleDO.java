package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:07:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:07:44
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ApplicationProfileRoleDO {

    @ApiModelProperty("应用")
    private ApplicationDO application;
    @ApiModelProperty("环境")
    private ProfileDO profile;
}
