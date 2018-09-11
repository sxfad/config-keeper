package com.suixingpay.config.server.entity;

import com.suixingpay.config.server.enums.SourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 全局配置修改日志
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午4:05:26
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午4:05:26
 */
@Data
@Accessors(chain = true)
@ApiModel
public class GlobalConfigLogDO implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("环境")
    private ProfileDO profile;
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
    @ApiModelProperty("操作时间")
    private Date createdDate;
}
