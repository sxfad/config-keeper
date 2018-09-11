package com.suixingpay.config.server.entity;

import com.suixingpay.config.server.enums.SourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 应用配置
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午3:59:52
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午3:59:52
 */
@Data
@ApiModel
@Accessors(chain = true)
public class ApplicationConfigDO implements BasePropertySourceDO, Serializable {

    private static final long serialVersionUID = 4715750867394494597L;
    @ApiModelProperty("环境spring.profiles.active")
    private ProfileDO profile;
    @ApiModelProperty("spring.application.name")
    private ApplicationDO application;
    @ApiModelProperty("配置信息")
    private String propertySource;
    @ApiModelProperty("配置类型")
    private SourceType sourceType;
    @ApiModelProperty("版本，每修改一次版本号加1，并添加新的一条记录")
    private Integer version;
    @ApiModelProperty("备注说明")
    private String memo;
    @ApiModelProperty("修改时间")
    private Date modifyTime;
    @ApiModelProperty("修改用户")
    private UserDO user;
}
