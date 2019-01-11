package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.entity.UserApplicationConfigRoleDO;
import com.suixingpay.config.server.security.PreAuthorize;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
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
 * @date: 2017年9月8日 下午6:30:02
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午6:30:02
 */
@Api(description = "用户应用关系权限接口")
@RestController
@RequestMapping("/userapplicationconfigrole")
public class UserApplicationConfigRoleController {

    @Autowired
    private UserApplicationConfigRoleService userApplicationConfigRoleService;

    @GetMapping("/{userId}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "查询用户应用关系权限", notes = "查询用户应用关系权限")
    public ResponseDTO<List<UserApplicationConfigRoleDO>> detail(
            @ApiParam(name = "userId", value = "userId", required = true) //
            @PathVariable("userId") @Valid @NotNull(message = "ID不能为空") Integer userId) {
        List<UserApplicationConfigRoleDO> userApplicationConfigRoleDOS = userApplicationConfigRoleService
                .listByUserId(userId);
        return new ResponseDTO<List<UserApplicationConfigRoleDO>>(userApplicationConfigRoleDOS);
    }
}
