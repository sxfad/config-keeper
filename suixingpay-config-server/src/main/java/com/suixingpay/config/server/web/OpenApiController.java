package com.suixingpay.config.server.web;

import com.suixingpay.config.common.Constant;
import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import com.suixingpay.config.server.entity.ApplicationInstanceDO;
import com.suixingpay.config.server.entity.BasePropertySourceDO;
import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.service.ApplicationConfigService;
import com.suixingpay.config.server.service.ApplicationInstanceService;
import com.suixingpay.config.server.service.GlobalConfigService;
import com.suixingpay.config.server.util.PropertySourceUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * 开放接口
 *
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月1日 上午10:21:55
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月1日 上午10:21:55
 */
@Slf4j
@Api(description = "开放接口")
@RestController
@RequestMapping("/open")
public class OpenApiController {

    private static final String GLOBAL_CONFIG = "globalConfig";

    private static final String APPLICATION_CONFIG = "applicationConfig";

    private static final int UNKNOWN_VERSION = -1;

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ApplicationInstanceService applicationInstanceService;

    @ApiOperation(value = "获取配置版本", notes = "获取配置版本")
    @GetMapping("/version")
    public VersionDTO getVersion(//
                                 @ApiParam(name = "application", value = "应用名", required = true) @RequestParam(required = true)
                                 //
                                 @Valid @NotEmpty(message = "应用名不能为空") String application, //
                                 @ApiParam(name = "profile", value = "profile", required = true) @RequestParam(required = true)
                                 //
                                 @Valid @NotEmpty(message = "profile不能为空") String profile) {
        GlobalConfigDO globalConfigDO = globalConfigService.getByProfileForOpenApi(profile);
        ApplicationConfigDO applicationConfigDO = applicationConfigService.getForOpenApi(application, profile);
        VersionDTO version = new VersionDTO();
        if (null != globalConfigDO) {
            version.setGlobalConfigVersion(globalConfigDO.getVersion());
        } else {
            version.setGlobalConfigVersion(UNKNOWN_VERSION);
        }
        if (null != applicationConfigDO) {
            version.setApplicationConfigVersion(applicationConfigDO.getVersion());
        } else {
            version.setApplicationConfigVersion(UNKNOWN_VERSION);
        }
        return version;
    }

    @ApiOperation(value = "获取全局配置", notes = "获取全局配置")
    @GetMapping("/{profile}")
    public PropertySource getGlobalConfig(@PathVariable @Valid @NotBlank String profile, //
                                          @ApiParam(name = "version", value = "版本号", required = false) @RequestParam(required = false) Integer version,
                                          HttpServletResponse response) {
        GlobalConfigDO globalConfigDO = globalConfigService.getByProfileForOpenApi(profile);
        // 如果没有修改，返回304以节约带宽
        if (isNotModified(globalConfigDO, version)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return null;
        }
        if (null == globalConfigDO) {
            return new PropertySource(GLOBAL_CONFIG, UNKNOWN_VERSION, null);
        }
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(globalConfigDO);
            return new PropertySource(GLOBAL_CONFIG, globalConfigDO.getVersion(), source);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @ApiOperation(value = "获取应用配置", notes = "获取应用配置")
    @GetMapping("/{application}/{profile}")
    public PropertySource getApplicationConfig(@PathVariable @Valid @NotBlank String application,
                                               @PathVariable @Valid @NotBlank String profile, //
                                               @ApiParam(name = "version", value = "版本号", required = false) @RequestParam(required = false) Integer version,//
                                               @ApiParam(name = Constant.IP_ADDRESS_HEADER, value = "IP地址", required = false) @RequestHeader(name = Constant.IP_ADDRESS_HEADER, required = false) String ipAddress,//
                                               @ApiParam(name = Constant.MANAGEMENT_PORT_HEADER, value = "management port", required = false) @RequestHeader(name = Constant.MANAGEMENT_PORT_HEADER, required = false) Integer managementPort,//
                                               @ApiParam(name = Constant.MANAGEMENT_CONTEXT_PATH_HEADER, value = "management context path", required = false) @RequestHeader(name = Constant.MANAGEMENT_CONTEXT_PATH_HEADER, required = false) String managerPath,//
                                               HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("ip={};managementPort={};managerPath={}", ipAddress, managementPort, managerPath);
        }
        ApplicationConfigDO applicationConfigDO = applicationConfigService.getForOpenApi(application, profile);
        if (null != applicationConfigDO && null != ipAddress && !ipAddress.isEmpty() && null != managementPort) {
            //存储应用实例信息
            ApplicationInstanceDO applicationInstanceDO = new ApplicationInstanceDO();
            applicationInstanceDO.setApplicationName(application);
            applicationInstanceDO.setProfile(profile);
            applicationInstanceDO.setIp(ipAddress);
            applicationInstanceDO.setPort(Integer.valueOf(managementPort));
            applicationInstanceDO.setManagerPath(managerPath);
            if (log.isDebugEnabled()) {
                log.debug("开启存储实例信息 instance={}", applicationInstanceDO);
            }
            applicationInstanceService.save(applicationInstanceDO);
        }
        // 如果没有修改，返回304以节约带宽
        if (isNotModified(applicationConfigDO, version)) {
            response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            return null;
        }
        if (null == applicationConfigDO) {
            return new PropertySource(APPLICATION_CONFIG, UNKNOWN_VERSION, null);
        }
        try {
            Map<String, Object> source = PropertySourceUtil.toPropertySource(applicationConfigDO);
            return new PropertySource(APPLICATION_CONFIG, applicationConfigDO.getVersion(), source);
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 检查内空是否被修改
     *
     * @param propertySourceDO
     * @param version
     * @return
     */
    private boolean isNotModified(BasePropertySourceDO propertySourceDO, Integer version) {
        if (null == version) {
            return false;
        }
        int latestVesion = -1;
        if (null != propertySourceDO && null != propertySourceDO.getVersion()) {
            latestVesion = propertySourceDO.getVersion();
        }
        return version.intValue() == latestVesion;
    }

}
