package com.suixingpay.config.server.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 14时17分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 14时17分
 */
@Data
@ApiModel
public class ApplicationInstanceDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("应用实例id")
    private Long id;

    @ApiModelProperty("应用名称")
    private String applicationName;

    @ApiModelProperty("应用环境")
    private String profile;

    @ApiModelProperty("ip地址")
    private String ip;

    @ApiModelProperty("端口")
    private Integer port;

    @ApiModelProperty("manager context path")
    private String managerPath;

    @ApiModelProperty("manager用户名")
    private String username;

    @ApiModelProperty("manager密码")
    private String password;

    @ApiModelProperty("修改时间")
    private Date modifyTime;

}
