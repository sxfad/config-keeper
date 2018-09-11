package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 应用
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午3:50:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午3:50:52
 */
@Data
@Accessors(chain = true)
@ApiModel
@NoArgsConstructor
public class ApplicationDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("应用名称（spring.application.name）")
    private String name;
    @ApiModelProperty("显示名称")
    private String description;
}
