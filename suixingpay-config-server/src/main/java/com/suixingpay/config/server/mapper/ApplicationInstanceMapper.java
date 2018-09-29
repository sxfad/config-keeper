package com.suixingpay.config.server.mapper;

import com.suixingpay.config.server.condition.ApplicationInstanceCondition;
import com.suixingpay.config.server.entity.ApplicationInstanceDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 14时35分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 14时35分
 */
public interface ApplicationInstanceMapper {

    List<ApplicationInstanceDO> list(ApplicationInstanceCondition condition);

    ApplicationInstanceDO findOne(ApplicationInstanceDO applicationInstanceDO);

    ApplicationInstanceDO findById(@Param("id") Long id);

    void addInstance(ApplicationInstanceDO applicationInstanceDO);

    void update(ApplicationInstanceDO form);

    void delete(Long id);
}
