package com.suixingpay.config.server.condition;

import org.springframework.data.domain.Pageable;

/**
 * 分页查询，查询条件
 *
 * @author jiayu.qiu
 */
public interface PageableCondition {

    /**
     * 获取分页条件
     *
     * @return
     */
    Pageable getPageable();
}
