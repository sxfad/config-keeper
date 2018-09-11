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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 修改用户信息表单
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午9:57:40
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午9:57:40
 */
@Data
@ApiModel
public class UpdateUserForm {
    @ApiModelProperty(value = "ID", required = true)
    @NotNull
    @Min(value = 1)
    private Integer id;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "请输入用户名!")
    @Length(max = 40, message = "用户名不能超过{max}个字符")
    private String name;

    @ApiModelProperty(value = "登录密码", required = false) // 如果不输入密码，则保留旧密码
    @Length(min = 6, max = 40, message = "登录密码必须大于{min}位，且不能超过{max}位")
    private String password;

    @ApiModelProperty(value = "邮箱", required = false)
    @Email(message = "邮箱格式不正确！")
    @Length(max = 40, message = "邮箱不能超过{max}个字符")
    private String email;

    @ApiModelProperty(value = "超级管理员", required = false)
    private YesNo administrator;

    @ApiModelProperty(value = "状态", required = false)
    private Status status;

    @ApiModelProperty(value = "应用环境权限关系", required = false)
    private List<ApplicationProfileRoleDO> applicationProfileRoles;

    @ApiModelProperty(value = "全局环境权限", required = false)
    private List<ProfileDO> profileRoles;

    public UserDO convertToUserDO() {
        return UserDO.builder().id(id).name(name.trim()).password(PasswordUtil.getMd5Password(password)).email(email)
                .administrator(administrator).status(status).build();
    }
}
