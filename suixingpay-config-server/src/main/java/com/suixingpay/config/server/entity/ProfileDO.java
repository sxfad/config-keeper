package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 环境表
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午3:29:57
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午3:29:57
 */
@Data
@Accessors(chain = true)
@ApiModel
public class ProfileDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * spring profile
     **/
    @ApiModelProperty(value = "profile（spring profile）")
    private String profile;
    /**
     * 中文名称
     **/
    @ApiModelProperty(value = "环境名称（用于页面显示）")
    private String name;
}
