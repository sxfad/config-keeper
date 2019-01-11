package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.entity.UserGlobalConfigRoleDO;
import com.suixingpay.config.server.security.PreAuthorize;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import com.suixingpay.config.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年10月16日 下午6:09:48
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年10月16日 下午6:09:48
 */
@Api(description = "用户环境（全局配置）关系权限接口")
@RestController
@RequestMapping("/userglobalconfigrole")
public class UserGlobalConfigRoleController {

    @Autowired
    private UserGlobalConfigRoleService userGlobalConfigRoleService;

    @GetMapping("/{userId}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "查询用户环境（全局配置）关系权限", notes = "查询用户环境（全局配置）关系权限")
    public ResponseDTO<List<UserGlobalConfigRoleDO>> detail(
            @ApiParam(name = "userId", value = "userId", required = true) //
            @PathVariable("userId") @Valid @NotNull(message = "ID不能为空") Integer userId) {
        List<UserGlobalConfigRoleDO> userGlobalConfigRoleDOS = userGlobalConfigRoleService.listByUserId(userId);
        return new ResponseDTO<List<UserGlobalConfigRoleDO>>(userGlobalConfigRoleDOS);
    }
}
