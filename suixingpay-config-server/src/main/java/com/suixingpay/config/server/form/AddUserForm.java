package com.suixingpay.config.server.form;

import com.suixingpay.config.server.entity.ApplicationProfileRoleDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.enums.Status;
import com.suixingpay.config.server.enums.YesNo;
import com.suixingpay.config.server.util.PasswordUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 添加用户表单
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午9:35:49
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午9:35:49
 */
@ApiModel
@Data
public class AddUserForm {
    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "请输入用户名!")
    @Length(max = 40, message = "用户名不能超过{max}个字符")
    private String name;

    @ApiModelProperty(value = "登录密码", required = true)
    @NotBlank(message = "请输入登录密码!")
    @Length(min = 6, max = 40, message = "登录密码必须大于{min}位，且不能超过{max}位")
    private String password;

    @ApiModelProperty(value = "邮箱", required = true)
    @NotBlank(message = "请输入邮箱!")
    @Email(message = "邮箱格式不正确！")
    @Length(max = 40, message = "邮箱不能超过{max}个字符")
    private String email;

    @ApiModelProperty(value = "超级管理员", required = false)
    private YesNo administrator;

    @ApiModelProperty(value = "应用环境权限关系", required = false)
    private List<ApplicationProfileRoleDO> applicationProfileRoles;

    @ApiModelProperty(value = "全局环境权限", required = false)
    private List<ProfileDO> profileRoles;

    public UserDO convertToUserDO() {
        return UserDO.builder().name(name.trim()).password(PasswordUtil.getMd5Password(password)).email(email.trim())
                .administrator(null == administrator ? YesNo.NO : administrator).status(Status.VALID).build();
    }
}
