package com.suixingpay.config.server.form;

import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月11日 上午10:04:33
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月11日 上午10:04:33
 */
@Data
@ApiModel
public class ApplicationConfigQueryForm {

    @ApiModelProperty(value = "应用名", required = false)
    private String applicationName;

    @ApiModelProperty(value = "环境名", required = false)
    private String profileName;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    public ApplicationConfigCondition convertToApplicationConfigCondition() {
        String searchKey = applicationName; // 模糊关键字
        String applicationName1 = null; // 完整的应用名
        if (null != applicationName && applicationName.length() > 0) {
            int beginInd = applicationName.indexOf("(");
            int endInd = applicationName.indexOf(")");
            if (beginInd != -1 && beginInd > 0) {
                if (endInd - beginInd > 1) {
                    // 如果括号中有内容，则是根据应用名，完全匹配查询，不是模糊匹配
                    applicationName1 = applicationName.substring(beginInd + 1, endInd);
                    searchKey = null;
                } else {
                    searchKey = applicationName.substring(0, beginInd);
                }
            }
        }
        ApplicationDO applicationDO = new ApplicationDO();
        applicationDO.setName(applicationName1);

        ProfileDO profileDO = new ProfileDO();
        profileDO.setProfile(profileName);

        ApplicationConfigCondition applicationConfigCondition = new ApplicationConfigCondition();
        applicationConfigCondition.setApplication(applicationDO).setProfile(profileDO);
        applicationConfigCondition.setSearchKey(searchKey);

        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        applicationConfigCondition.setPageable(pageable);
        return applicationConfigCondition;
    }
}
