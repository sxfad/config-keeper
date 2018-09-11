package com.suixingpay.config.server.condition;

import com.suixingpay.config.server.entity.ApplicationDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;

/**
 * 查询条件
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午9:31:54
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午9:31:54
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ApplicationCondition extends ApplicationDO implements PageableCondition {
    private static final long serialVersionUID = 1L;

    private Pageable pageable;

    @ApiModelProperty("查询关键字")
    private String searchKey;

    /**
     * @return
     * @see com.suixingpay.config.server.condition.PageableCondition#getPageable()
     */
    @Override
    public Pageable getPageable() {
        return this.pageable;
    }
}
