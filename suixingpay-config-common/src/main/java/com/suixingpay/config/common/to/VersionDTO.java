package com.suixingpay.config.common.to;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午4:32:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午4:32:32
 */
@Data
@ApiModel
public class VersionDTO {
    @ApiModelProperty()
    private int globalConfigVersion;
    @ApiModelProperty()
    private int applicationConfigVersion;
}
