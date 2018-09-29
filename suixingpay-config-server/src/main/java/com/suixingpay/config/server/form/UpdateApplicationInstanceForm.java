package com.suixingpay.config.server.form;

import com.suixingpay.config.server.entity.ApplicationInstanceDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 15时37分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 15时37分
 */
@Data
@ApiModel
public class UpdateApplicationInstanceForm {

    @ApiModelProperty(value = "实例id", required = true)
    @NotNull(message = "实例id不能为空")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空！")
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空！")
    private String password;

    public ApplicationInstanceDO convertToApplicationInstanceDO() {
        ApplicationInstanceDO instanceDO = new ApplicationInstanceDO();
        instanceDO.setId(this.id);
        instanceDO.setUsername(this.username);
        instanceDO.setPassword(this.password);
        return instanceDO;
    }
}
