package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:08:00
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:08:00
 */
@Data
@ApiModel
@Builder
public class MenuDO {
    @ApiModelProperty(value = "菜单key")
    private String key;

    @ApiModelProperty(value = "父节点key")
    private String parentKey;

    @ApiModelProperty(value = "菜单名称")
    private String text;

    @ApiModelProperty(value = "菜单key")
    private String icon;

    @ApiModelProperty(value = "路径")
    private String path;
}
