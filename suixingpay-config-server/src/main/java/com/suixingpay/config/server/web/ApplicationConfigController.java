package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.condition.ApplicationConfigCondition;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import com.suixingpay.config.server.entity.ApplicationConfigLogDO;
import com.suixingpay.config.server.entity.ApplicationDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.form.ApplicationConfigForm;
import com.suixingpay.config.server.form.ApplicationConfigQueryForm;
import com.suixingpay.config.server.service.ApplicationConfigLogService;
import com.suixingpay.config.server.service.ApplicationConfigService;
import com.suixingpay.config.server.service.UserApplicationConfigRoleService;
import com.suixingpay.config.server.util.PropertySourceUtil;
import com.suixingpay.config.server.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月8日 下午6:31:00
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午6:31:00
 */
@Slf4j
@Api(description = "应用配置")
@RestController
@RequestMapping("/applicationconfig")
public class ApplicationConfigController {

    private static final String SOURCE_ERROR = "配置内容不正确";

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Autowired
    private UserApplicationConfigRoleService userApplicationConfigRoleService;

    @Autowired
    private ApplicationConfigLogService applicationConfigLogService;

    @GetMapping
    @ApiOperation(value = "配置列表", notes = "配置列表")
    public ResponseDTO<Page<ApplicationConfigDO>> list(HttpServletRequest request, //
                                                       ApplicationConfigQueryForm applicationConfigQueryForm) {
        ApplicationConfigCondition condition = applicationConfigQueryForm.convertToApplicationConfigCondition();
        UserDO userDO = SecurityUtil.getSessionUser(request).get();
        condition.setUser(userDO);
        Page<ApplicationConfigDO> pageRes = this.applicationConfigService.pageByCondition(condition);
        return new ResponseDTO<Page<ApplicationConfigDO>>(pageRes);
    }

    @GetMapping("/{application}/{profile}")
    @ApiOperation(value = "详情", notes = "详情")
    public ResponseDTO<ApplicationConfigDO> detail(HttpServletRequest request, //
                                                   @ApiParam(name = "application", value = "应用名称", required = true) //
                                                   @PathVariable("application") @Valid @NotBlank(message = "应用名称不能为空") String applicationName, //
                                                   @ApiParam(name = "profile", value = "profile", required = true) //
                                                   @PathVariable("profile") @Valid @NotBlank(message = "profile不能为空") String profile) {
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userApplicationConfigRoleService.checkHasRole(user.get(), applicationName, profile);
        ApplicationConfigDO applicationConfigDO = this.applicationConfigService
                .getByApplicationNameAnddProfile(applicationName, profile);
        return new ResponseDTO<ApplicationConfigDO>(applicationConfigDO);
    }

    @PostMapping()
    @ApiOperation(value = "添加配置", notes = "添加配置")
    public ResponseDTO<Void> addApplicationConfig(HttpServletRequest request, //
                                                  @RequestBody @Valid ApplicationConfigForm form) throws Exception {
        ApplicationConfigDO applicationConfigDO = form.convertToApplicationConfigDO();
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(applicationConfigDO);
            if (null == source || source.isEmpty()) {
                return new ResponseDTO<Void>().addErrorMessage(SOURCE_ERROR);
            }
        } catch (Throwable e) {
            return new ResponseDTO<Void>().addErrorMessage(e.getMessage());
        }

        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userApplicationConfigRoleService.checkHasRole(user.get(), form.getApplication(), form.getProfile());
        applicationConfigDO.setUser(user.get());
        if (null != applicationConfigService.getByApplicationNameAnddProfile(form.getApplication(),
                form.getProfile())) {
            return new ResponseDTO<Void>().addErrorMessage("配置已存在！");
        }
        this.applicationConfigService.saveApplicationConfig(applicationConfigDO);
        return new ResponseDTO<Void>();
    }

    @PutMapping()
    @ApiOperation(value = "修改配置", notes = "修改配置")
    public ResponseDTO<Void> updateApplicationConfig(HttpServletRequest request, //
                                                     @RequestBody @Valid ApplicationConfigForm form) throws Exception {
        ApplicationConfigDO applicationConfigDO = form.convertToApplicationConfigDO();
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(applicationConfigDO);
            if (null == source || source.isEmpty()) {
                return new ResponseDTO<Void>().addErrorMessage(SOURCE_ERROR);
            }
        } catch (Throwable e) {
            return new ResponseDTO<Void>().addErrorMessage(e.getMessage());
        }
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userApplicationConfigRoleService.checkHasRole(user.get(), form.getApplication(), form.getProfile());
        applicationConfigDO.setUser(user.get());
        this.applicationConfigService.saveApplicationConfig(applicationConfigDO);
        return new ResponseDTO<Void>();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "历史数据替换当前配置", notes = "历史数据替换当前配置")
    public ResponseDTO<Void> replaceApplicationConfig(HttpServletRequest request, //
                                                      @ApiParam(name = "id", value = "历史数据id", required = true) //
                                                      @PathVariable("id") @Valid @NotNull(message = "历史数据id不能为空") Long id) throws Exception {

        ApplicationConfigLogDO applicationConfigLogDO = this.applicationConfigLogService.getById(id);
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userApplicationConfigRoleService.checkHasRole(user.get(), applicationConfigLogDO.getApplication().getName(),
                applicationConfigLogDO.getProfile().getProfile());
        ApplicationConfigDO applicationConfigDO = new ApplicationConfigDO();
        applicationConfigDO.setUser(user.get());
        applicationConfigDO.setApplication(applicationConfigLogDO.getApplication());
        applicationConfigDO.setMemo(
                String.format("%s;从 %d 版本回退！", applicationConfigLogDO.getMemo(), applicationConfigLogDO.getVersion()));
        applicationConfigDO.setProfile(applicationConfigLogDO.getProfile());
        applicationConfigDO.setPropertySource(applicationConfigLogDO.getPropertySource());
        applicationConfigDO.setSourceType(applicationConfigLogDO.getSourceType());

        this.applicationConfigService.replaceApplicationConfig(applicationConfigDO);
        return new ResponseDTO<Void>();
    }

    /**
     * 检查主键是否重复
     *
     * @param profile
     * @return
     */
    @GetMapping("/checkKeyUnique")
    @ApiOperation(value = "检查主键是否重复", notes = "检查主键是否重复")
    public ResponseDTO<Boolean> checkKeyUnique(
            @Valid @NotBlank(message = "profile不能为空") @RequestParam("profile") String profile,
            @Valid @NotBlank(message = "applicationName不能为空") @RequestParam("applicationName") String applicationName) {

        if (null == applicationConfigService.getByApplicationNameAnddProfile(applicationName, profile)) {
            // 不重复 可用
            return new ResponseDTO<Boolean>(false);
        }
        // 重复 不可用
        return new ResponseDTO<Boolean>(true);
    }

    /**
     * 获取当前用户可管理的Profiles
     *
     * @param request
     * @return
     */
    @GetMapping("/profiles")
    @ApiOperation(value = "获取当前用户可管理的Profiles", notes = "获取当前用户可管理的Profiles")
    public ResponseDTO<List<ProfileDO>> myProfiles(HttpServletRequest request) {
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        List<ProfileDO> profileDOs = userApplicationConfigRoleService.getProfilesByUser(user.get());
        return new ResponseDTO<List<ProfileDO>>(profileDOs);
    }

    /**
     * 获取当前用户可管理的Applications
     *
     * @param request
     * @return
     */
    @GetMapping("/{profile}/applications")
    @ApiOperation(value = "获取当前用户可管理的Applications", notes = "获取当前用户可管理的Applications")
    public ResponseDTO<List<ApplicationDO>> myApplications(HttpServletRequest request, //
                                                           @ApiParam(name = "profile", value = "profile", required = true)
                                                           //
                                                           @PathVariable("profile") @Valid @NotBlank(message = "profile不能为空") String profile, //
                                                           @ApiParam(name = "searchKey", value = "搜索关键字", required = false)
                                                           //
                                                           @RequestParam(value = "searchKey", required = false) String searchKey) {
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        String applicationName = null;
        if (null != searchKey && searchKey.length() > 0) {
            int beginInd = searchKey.indexOf("(");
            int endInd = searchKey.indexOf(")");
            if (beginInd != -1 && beginInd > 0) {
                if (endInd - beginInd > 1) {
                    // 如果括号中有内容，则是根据应用名，完全匹配查询，不是模糊匹配
                    applicationName = searchKey.substring(beginInd + 1, endInd);
                    searchKey = null;
                } else {
                    searchKey = searchKey.substring(0, beginInd);
                }
            }
        }
        log.debug("searchKey = {}", searchKey);
        List<ApplicationDO> applicationDOs = userApplicationConfigRoleService
                .getApplicationsByUserAndProfile(user.get(), profile, searchKey, applicationName);
        return new ResponseDTO<List<ApplicationDO>>(applicationDOs);
    }
}
