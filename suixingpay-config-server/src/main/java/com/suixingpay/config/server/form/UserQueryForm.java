package com.suixingpay.config.server.form;

import com.suixingpay.config.server.condition.UserCondition;
import com.suixingpay.config.server.enums.Status;
import com.suixingpay.config.server.enums.YesNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年11月17日 下午3:18:02
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年11月17日 下午3:18:02
 */
@Data
@ApiModel
public class UserQueryForm {

    @ApiModelProperty(value = "用户名", required = true)
    private String name;

    @ApiModelProperty(value = "角色")
    private YesNo administrator;

    @ApiModelProperty(value = "状态")
    private Status status;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    public UserCondition convertToUserCondition() {
        UserCondition condition = new UserCondition();
        condition.setName(name);
        condition.setStatus(status);
        condition.setAdministrator(administrator);

        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        condition.setPageable(pageable);
        return condition;
    }
}
