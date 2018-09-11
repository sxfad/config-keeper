package com.suixingpay.config.server.form;

import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月17日 下午2:47:59
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月17日 下午2:47:59
 */
@Data
@ApiModel
public class ApplicationConfigLogQueryForm {

    @ApiModelProperty(value = "应用名", required = false)
    private String applicationName;

    @ApiModelProperty(value = "环境名", required = false)
    private String profileName;

    @ApiModelProperty(value = "最小版本号", required = false)
    private Integer minVersion;

    @ApiModelProperty(value = "最大版本号", required = false)
    private Integer maxVersion;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    public ApplicationConfigLogCondition convertToApplicationConfigLogCondition() {
        ApplicationConfigLogCondition condition = new ApplicationConfigLogCondition();

        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName(applicationName);

        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile(profileName);

        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        condition.setPageable(pageable);
        condition.setMaxVersion(maxVersion);
        condition.setMinVersion(minVersion);

        condition.setApplication(applicationDO).setProfile(profileDO);
        return condition;
    }
}
