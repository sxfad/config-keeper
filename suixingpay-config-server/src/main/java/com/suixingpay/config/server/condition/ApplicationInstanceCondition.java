package com.suixingpay.config.server.condition;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 17时56分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 17时56分
 */
@Data
@ApiModel
public class ApplicationInstanceCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank(message = "应用名称不能为空")
    private String applicationName;

    @NotBlank(message = "环境不能为空")
    private String profile;

}
