package com.suixingpay.config.server.form;

import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.util.MiscUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * 添加修改应用表单
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:08:27
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:08:27
 */
@ApiModel
@Data
public class AddEditApplicationForm {
    @ApiModelProperty(value = "应用名称", required = true)
    @NotBlank(message = "应用名称不能为空")
    @Length(max = 40, message = "应用名称长度不能超过{max}位字符")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "只能输入英文大小写，下划线，中划线和数字")
    private String name;

    @ApiModelProperty(value = "应用显示名称", required = true) //
    @Length(max = 40, message = "显示名称长度不能超过{max}位字符")
    private String description;

    public ApplicationDO convertToApplicationDO() throws Exception {
        checkData();
        return new ApplicationDO().setName(name.trim()).setDescription(description.trim());
    }

    private void checkData() throws Exception {
        if (MiscUtil.isHashBrackets(name)) {
            throw new Exception("应用名称不带有括号");
        }
        if (MiscUtil.isHashBrackets(description)) {
            throw new Exception("应用显示名称不带有括号");
        }
    }
}
