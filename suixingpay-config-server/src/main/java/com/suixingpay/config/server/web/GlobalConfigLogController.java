package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.condition.GlobalConfigLogCondition;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.form.GlobalConfigLogQueryForm;
import com.suixingpay.config.server.service.GlobalConfigLogService;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import com.suixingpay.config.server.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月8日 下午6:30:28
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午6:30:28
 */
@Api(description = "全局配置日志")
@RestController
@RequestMapping("/globalconfiglog")
public class GlobalConfigLogController {
    @Autowired
    private GlobalConfigLogService globalConfigLogService;

    @Autowired
    private UserGlobalConfigRoleService userGlobalConfigRoleService;

    @GetMapping
    @ApiOperation(value = "配置日志列表", notes = "配置日志列表")
    public ResponseDTO<Page<GlobalConfigLogDO>> list(HttpServletRequest request, //
                                                     GlobalConfigLogQueryForm form) {
        UserDO userDO = SecurityUtil.getSessionUser(request).get();

        GlobalConfigLogCondition condition = form.convertToGlobalConfigLogCondition();
        // 根据用户进行限制查询范围
        condition.setUser(userDO);
        Page<GlobalConfigLogDO> pageRes = this.globalConfigLogService.pageByCondition(condition);
        return new ResponseDTO<Page<GlobalConfigLogDO>>(pageRes);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "详情", notes = "详情")
    public ResponseDTO<GlobalConfigLogDO> detail(HttpServletRequest request, //
                                                 @ApiParam(name = "id", value = "ID", required = true) //
                                                 @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Long id) {

        GlobalConfigLogDO globalConfigLogDO = this.globalConfigLogService.getById(id);
        if (null != globalConfigLogDO) {
            UserDO userDO = SecurityUtil.getSessionUser(request).get();
            userGlobalConfigRoleService.checkHasRole(userDO, globalConfigLogDO.getProfile().getProfile());
        }
        return new ResponseDTO<GlobalConfigLogDO>(globalConfigLogDO);
    }

}
