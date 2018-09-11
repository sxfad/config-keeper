package com.suixingpay.cloud.test.service;

import com.suixingpay.cloud.test.dto.UserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月7日 下午5:11:34
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月7日 下午5:11:34
 */
@Service
@Slf4j
@RefreshScope
public class UserServiceImpl implements UserService {

    public UserServiceImpl() {
        log.info("UserServiceImpl init ... ...");
    }

    @Override
    public UserDO getUserById(Integer id) {
        // TODO Auto-generated method stub
        return null;
    }

}
