package com.suixingpay.config.server.form;

import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.util.MiscUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:08:45
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:08:45
 */
@ApiModel
@Data
public class AddEditProfileForm {
    @ApiModelProperty(value = "profile", required = true) //
    @NotBlank(message = "profile不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "只能输入英文大小写字母和数字")
    private String profile;

    @ApiModelProperty(value = "name", required = true) //
    @NotBlank(message = "name不能为空")
    @Length(max = 40, message = "name长度不能超过{max}位字符")
    private String name;

    public ProfileDO convertToProfileDO() throws Exception {
        checkData();
        return new ProfileDO().setName(name.trim()).setProfile(profile.trim());
    }

    private void checkData() throws Exception {
        if (MiscUtil.isHashBrackets(profile)) {
            throw new Exception("环境名称不带有括号");
        }
        if (MiscUtil.isHashBrackets(name)) {
            throw new Exception("显示名称不带有括号");
        }
    }
}
