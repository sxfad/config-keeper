package com.suixingpay.config.server.condition;

import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午2:55:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午2:55:32
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class GlobalConfigLogCondition extends GlobalConfigLogDO implements PageableCondition {

    private static final long serialVersionUID = 1L;

    private Pageable pageable;

    @ApiModelProperty("最小版本号")
    private Integer minVersion;

    @ApiModelProperty("最大版本号")
    private Integer maxVersion;

    /**
     * @return
     * @see com.suixingpay.config.server.condition.PageableCondition#getPageable()
     */
    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

}
