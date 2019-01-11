package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.condition.ApplicationConfigLogCondition;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.form.ApplicationConfigLogQueryForm;
import com.suixingpay.config.server.service.ApplicationConfigLogService;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
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
 * @date: 2017年9月8日 下午6:31:44
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午6:31:44
 */
@Api(description = "应用配置日志")
@RestController
@RequestMapping("/applicationconfiglog")
public class ApplicationConfigLogController {
    @Autowired
    private ApplicationConfigLogService applicationConfigLogService;

    @Autowired
    private UserApplicationConfigRoleService userApplicationConfigRoleService;

    @GetMapping
    @ApiOperation(value = "配置日志列表", notes = "配置日志列表")
    public ResponseDTO<Page<ApplicationConfigLogDO>> list(HttpServletRequest request, //
                                                          ApplicationConfigLogQueryForm form) {
        UserDO userDO = SecurityUtil.getSessionUser(request).get();
        ApplicationConfigLogCondition condition = form.convertToApplicationConfigLogCondition();
        // 根据用户进行限制查询范围
        condition.setUser(userDO);
        Page<ApplicationConfigLogDO> pageRes = this.applicationConfigLogService.pageByCondition(condition);
        return new ResponseDTO<Page<ApplicationConfigLogDO>>(pageRes);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "详情", notes = "详情")
    public ResponseDTO<ApplicationConfigLogDO> detail(HttpServletRequest request, //
                                                      @ApiParam(name = "id", value = "ID", required = true) //
                                                      @PathVariable("id") @Valid @NotNull(message = "ID不能为空") Long id) {

        ApplicationConfigLogDO applicationConfigLogDO = this.applicationConfigLogService.getById(id);
        if (null != applicationConfigLogDO) {
            UserDO userDO = SecurityUtil.getSessionUser(request).get();
            userApplicationConfigRoleService.checkHasRole(userDO, applicationConfigLogDO.getApplication().getName(),
                    applicationConfigLogDO.getProfile().getProfile());
        }
        return new ResponseDTO<ApplicationConfigLogDO>(applicationConfigLogDO);
    }
}
