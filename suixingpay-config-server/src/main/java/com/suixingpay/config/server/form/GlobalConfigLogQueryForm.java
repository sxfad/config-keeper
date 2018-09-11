package com.suixingpay.config.server.form;

import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
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
public class GlobalConfigLogQueryForm {

    @ApiModelProperty(value = "环境名", required = true)
    private String profileName;

    @ApiModelProperty(value = "最小版本号", required = false)
    private Integer minVersion;

    @ApiModelProperty(value = "最大版本号", required = false)
    private Integer maxVersion;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    public GlobalConfigLogCondition convertToGlobalConfigLogCondition() {
        GlobalConfigLogCondition condition = new GlobalConfigLogCondition();

        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile(profileName);
        condition.setProfile(profileDO);

        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        condition.setPageable(pageable);
        condition.setMaxVersion(maxVersion);
        condition.setMinVersion(minVersion);

        return condition;
    }
}
