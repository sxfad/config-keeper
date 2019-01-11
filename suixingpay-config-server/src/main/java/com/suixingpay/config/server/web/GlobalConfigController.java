package com.suixingpay.config.server.web;

import com.suixingpay.config.common.to.ResponseDTO;
import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.entity.GlobalConfigLogDO;
import com.suixingpay.config.server.entity.ProfileDO;
import com.suixingpay.config.server.entity.UserDO;
import com.suixingpay.config.server.form.GlobalConfigForm;
import com.suixingpay.config.server.service.GlobalConfigLogService;
import com.suixingpay.config.server.service.GlobalConfigService;
import com.suixingpay.config.server.service.UserGlobalConfigRoleService;
import com.suixingpay.config.server.util.PropertySourceUtil;
import com.suixingpay.config.server.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月8日 下午6:30:02
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月8日 下午6:30:02
 */
@Api(description = "全局配置")
@RestController
@RequestMapping("/globalconfig")
public class GlobalConfigController {

    private static final String SOURCE_ERROR = "配置内容不正确";

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private UserGlobalConfigRoleService userGlobalConfigRoleService;

    @Autowired
    private GlobalConfigLogService globalConfigLogService;

    @GetMapping
    @ApiOperation(value = "配置列表", notes = "配置列表")
    public ResponseDTO<List<GlobalConfigDO>> list(HttpServletRequest request) {
        UserDO userDO = SecurityUtil.getSessionUser(request).get();
        List<GlobalConfigDO> list = this.globalConfigService.listByUser(userDO);
        return new ResponseDTO<List<GlobalConfigDO>>(list);
    }

    @GetMapping("/{profile}")
    @ApiOperation(value = "详情", notes = "详情")
    public ResponseDTO<GlobalConfigDO> detail(HttpServletRequest request, //
                                              @ApiParam(name = "profile", value = "profile", required = true) //
                                              @PathVariable("profile") @Valid @NotBlank(message = "profile不能为空") String profile) {
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userGlobalConfigRoleService.checkHasRole(user.get(), profile);
        GlobalConfigDO globalConfigDO = this.globalConfigService.getByProfile(profile);
        return new ResponseDTO<GlobalConfigDO>(globalConfigDO);
    }

    @PostMapping()
    @ApiOperation(value = "添加配置", notes = "添加配置")
    public ResponseDTO<Void> addGlobalConfig(HttpServletRequest request, //
                                             @RequestBody @Valid GlobalConfigForm form) throws Exception {
        GlobalConfigDO globalConfigDO = form.convertToGlobalConfigDO();
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(globalConfigDO);
            if (null == source || source.isEmpty()) {
                return new ResponseDTO<Void>().addErrorMessage(SOURCE_ERROR);
            }
        } catch (Throwable e) {
            return new ResponseDTO<Void>().addErrorMessage(e.getMessage());
        }
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userGlobalConfigRoleService.checkHasRole(user.get(), form.getProfile());
        globalConfigDO.setUser(user.get());
        if (null != globalConfigService.getByProfile(form.getProfile())) {
            return new ResponseDTO<Void>().addErrorMessage("配置已存在！");
        }
        this.globalConfigService.saveGlobalConfig(globalConfigDO);
        return new ResponseDTO<Void>();
    }

    @PutMapping()
    @ApiOperation(value = "修改配置", notes = "修改配置")
    public ResponseDTO<Void> updateGlobalConfig(HttpServletRequest request, //
                                                @RequestBody @Valid GlobalConfigForm form) throws Exception {
        GlobalConfigDO globalConfigDO = form.convertToGlobalConfigDO();
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(globalConfigDO);
            if (null == source || source.isEmpty()) {
                return new ResponseDTO<Void>().addErrorMessage(SOURCE_ERROR);
            }
        } catch (Throwable e) {
            return new ResponseDTO<Void>().addErrorMessage(e.getMessage());
        }
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        userGlobalConfigRoleService.checkHasRole(user.get(), globalConfigDO.getProfile().getProfile());
        globalConfigDO.setUser(user.get());
        this.globalConfigService.saveGlobalConfig(globalConfigDO);
        return new ResponseDTO<Void>();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "历史记录替换当前配置", notes = "历史记录替换当前配置")
    public ResponseDTO<Void> replaceGlobalConfig(HttpServletRequest request, //
                                                 @ApiParam(name = "id", value = "历史数据id", required = true) //
                                                 @PathVariable("id") @Valid @NotNull(message = "历史数据id不能为空") Long id) throws Exception {

        GlobalConfigDO globalConfigDO = new GlobalConfigDO();
        GlobalConfigLogDO globalConfigLogDO = globalConfigLogService.getById(id);
        // 拼装信息
        Optional<UserDO> user = SecurityUtil.getSessionUser(request);
        globalConfigDO.setUser(user.get());
        globalConfigDO
                .setMemo(String.format("%s;从 %d 版本回退！", globalConfigLogDO.getMemo(), globalConfigLogDO.getVersion()));
        globalConfigDO.setProfile(globalConfigLogDO.getProfile());
        globalConfigDO.setPropertySource(globalConfigLogDO.getPropertySource());
        globalConfigDO.setSourceType(globalConfigLogDO.getSourceType());
        // 校验权限
        userGlobalConfigRoleService.checkHasRole(user.get(), globalConfigDO.getProfile().getProfile());
        // 更新信息
        this.globalConfigService.replaceGlobalConfig(globalConfigDO);
        return new ResponseDTO<Void>();
    }

    /**
     * 检查主键是否重复
     *
     * @param profile
     * @return
     */
    @GetMapping("/checkKeyUnique/{profile}")
    @ApiOperation(value = "检查主键是否重复", notes = "检查主键是否重复")
    public ResponseDTO<Boolean> checkKeyUnique(@PathVariable("profile") String profile) {
        if (null == globalConfigService.getByProfile(profile)) {
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
        List<ProfileDO> profileDOs = userGlobalConfigRoleService.getProfilesByUser(user.get());
        return new ResponseDTO<List<ProfileDO>>(profileDOs);
    }
}
