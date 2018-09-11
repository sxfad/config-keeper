package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.PageableCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询服务
 *
 * @param <C> 查询条件，必须是PageableCondition的子类
 * @param <R> 查询结果类
 * @author jiayu.qiu
 */
public interface PageableService<C extends PageableCondition, R> {

    /**
     * 分页查询
     *
     * @param condition 查询条件
     * @return 分页结果
     * @throws CloudtpException
     */
    default Page<R> pageByCondition(C condition) {
        Long totalCnt = countByCondition(condition);
        if (null == totalCnt || totalCnt.intValue() <= 0) {
            return null;
        }
        List<R> list = null;
        if (null != totalCnt && totalCnt.intValue() > 0) {
            list = listByCondition(condition);
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, condition.getPageable(), totalCnt);
    }

    /**
     * 根据查询条件，获得总记录数
     *
     * @param condition 查询条件
     * @return 总记录数
     */
    Long countByCondition(C condition);

    /**
     * 根据查询条件，获得数据列表
     *
     * @param condition 查询条件
     * @return 结果集
     */
    List<R> listByCondition(C condition);
}
