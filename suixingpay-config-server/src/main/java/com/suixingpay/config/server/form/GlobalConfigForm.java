package com.suixingpay.config.server.form;

import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.enums.SourceType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月11日 上午10:04:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月11日 上午10:04:33
 */
@Data
@ApiModel
public class GlobalConfigForm {
    @ApiModelProperty(value = "环境", required = true)
    @NotBlank(message = "请输入环境!")
    @Length(max = 10, message = "环境不能超过{max}个字符")
    private String profile;

    @ApiModelProperty(value = "配置类型", required = true)
    @NotNull(message = "配置类型不能为空")
    private SourceType sourceType;

    @ApiModelProperty(value = "配置信息", required = true)
    @NotBlank(message = "请输入配置信息!")
    private String propertySource;

    @ApiModelProperty("备注说明")
    private String memo;

    @ApiModelProperty("版本号")
    private Integer version;

    public GlobalConfigDO convertToGlobalConfigDO() {
        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile(profile.trim());

        GlobalConfigDO globalConfigDO = new GlobalConfigDO();
        globalConfigDO.setProfile(profileDO).setSourceType(sourceType).setPropertySource(propertySource).setMemo(memo)
                .setVersion(version);
        return globalConfigDO;
    }
}
