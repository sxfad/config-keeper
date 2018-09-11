package com.suixingpay.config.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.suixingpay.config.server.enums.Status;
import com.suixingpay.config.server.enums.YesNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户 密码不能传到客户端
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月30日 上午11:37:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月30日 上午11:37:48
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
@JsonIgnoreProperties("password")
public class UserDO implements Serializable {

    private static final long serialVersionUID = -4474405068594065456L;
    @ApiModelProperty(value = "ID")
    private Integer id;
    @ApiModelProperty(value = "登录名")
    private String name;
    @ApiModelProperty(value = "登录密码")
    private String password;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "是否超级管理员")
    private YesNo administrator;
    @ApiModelProperty(value = "状态")
    private Status status;
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    /**
     * 判断当前用户是否是超级管理员
     *
     * @return
     */
    public boolean isSuper() {
        return administrator == YesNo.YES;
    }
}
