package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.condition.ApplicationCondition;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.form.AddEditApplicationForm;
import com.suixingpay.config.server.form.ApplicationQueryForm;
import com.suixingpay.config.server.security.PreAuthorize;
import com.suixingpay.config.server.service.ApplicationService;
import com.suixingpay.config.server.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 应用管理
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午10:02:58
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午10:02:58
 */
@Api(description = "应用")
@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    /**
     * 应用列表
     *
     * @param form
     * @return
     */
    @GetMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "应用列表", notes = "应用列表")
    public ResponseDTO<Page<ApplicationDO>> list(ApplicationQueryForm form) {
        ApplicationCondition condition = form.convertToApplicationCondition();
        Page<ApplicationDO> list = this.applicationService.pageByCondition(condition);
        return new ResponseDTO<Page<ApplicationDO>>(list);
    }

    /**
     * 应用详情
     *
     * @param name
     * @return
     */
    @GetMapping("/{name}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "应用详情", notes = "应用详情")
    public ResponseDTO<ApplicationDO> detail(//
                                             @ApiParam(name = "name", value = "name", required = true) //
                                             @PathVariable("name") @Valid @NotBlank(message = "name不能为空") String name) {
        ApplicationDO applicationDO = this.applicationService.getByName(name);
        return new ResponseDTO<ApplicationDO>(applicationDO);
    }

    /**
     * 添加应用
     *
     * @param addEditApplicationForm
     * @return
     * @throws Exception
     */
    @PostMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "添加应用", notes = "添加应用")
    public ResponseDTO<Void> addApplication(@RequestBody @Validated AddEditApplicationForm addEditApplicationForm)
            throws Exception {
        ApplicationDO applicationDO = addEditApplicationForm.convertToApplicationDO();
        applicationService.addApplication(applicationDO);
        return new ResponseDTO<Void>();
    }

    /**
     * 修改应用
     *
     * @param addEditApplicationForm
     * @return
     * @throws Exception
     */
    @PutMapping
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "修改应用", notes = "修改应用")
    public ResponseDTO<Void> updateApplication(@RequestBody @Validated AddEditApplicationForm addEditApplicationForm)
            throws Exception {
        ApplicationDO applicationDO = addEditApplicationForm.convertToApplicationDO();
        applicationService.updateApplication(applicationDO);
        return new ResponseDTO<Void>();
    }

    /**
     * 检查主键是否重复
     *
     * @param name
     * @return
     */
    @GetMapping("/checkKeyUnique/{name}")
    @PreAuthorize(UserService.HAS_SUPER_ROLE)
    @ApiOperation(value = "检查主键是否重复", notes = "检查主键是否重复")
    public ResponseDTO<Boolean> checkKeyUnique(@PathVariable("name") String name) {
        if (null == applicationService.getByName(name)) {
            // 不重复 可用
            return new ResponseDTO<Boolean>(false);
        }
        // 重复 不可用
        return new ResponseDTO<Boolean>(true);
    }
}
