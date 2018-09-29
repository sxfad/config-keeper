package com.suixingpay.config.server.service;

import com.suixingpay.config.server.condition.ApplicationInstanceCondition;
import com.suixingpay.config.server.entity.ApplicationInstanceDO;

import java.util.List;

public interface ApplicationInstanceService {

    void save(ApplicationInstanceDO applicationInstanceDO);

    List<ApplicationInstanceDO> list(ApplicationInstanceCondition condition);

    void update(ApplicationInstanceDO applicationInstanceDO);

    ApplicationInstanceDO findById(Long id);

    void delete(Long id);
}
