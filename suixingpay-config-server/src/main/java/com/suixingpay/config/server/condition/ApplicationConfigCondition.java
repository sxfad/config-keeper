package com.suixingpay.config.server.condition;

import com.suixingpay.config.server.entity.ApplicationConfigDO;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月4日 下午2:55:32
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月4日 下午2:55:32
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = true)
public class ApplicationConfigCondition extends ApplicationConfigDO implements PageableCondition {

    private static final long serialVersionUID = 1L;

    private Pageable pageable;

    private String searchKey;

    @Override
    public Pageable getPageable() {
        return this.pageable;
    }

}
