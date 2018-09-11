package com.suixingpay.config.server.condition;

import com.suixingpay.config.server.entity.UserDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;

/**
 * 查询条件
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年8月31日 下午6:10:56
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年8月31日 下午6:10:56
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class UserCondition extends UserDO implements PageableCondition {

    private static final long serialVersionUID = -5820645414237916334L;

    private Pageable pageable;

    /**
     * @return
     * @see com.suixingpay.config.server.condition.PageableCondition#getPageable()
     */
    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

}
