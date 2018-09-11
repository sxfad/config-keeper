package com.suixingpay.config.server.form;

import com.suixingpay.config.server.condition.ApplicationCondition;
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
public class ApplicationQueryForm {

    @ApiModelProperty(value = "查询关键字", required = false)
    private String searchKey;

    @ApiModelProperty(value = "页码", required = true)
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示条数", required = true)
    private Integer pageSize;

    public ApplicationCondition convertToApplicationCondition() {
        ApplicationCondition condition = new ApplicationCondition();

        condition.setSearchKey(searchKey);
        Pageable pageable = new PageRequest(pageNum - 1, pageSize);
        condition.setPageable(pageable);
        return condition;
    }
}
