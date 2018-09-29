package com.suixingpay.config.server.web;

import com.suixingpay.config.common.Constant;
import com.suixingpay.config.common.to.VersionDTO;
import com.suixingpay.config.server.condition.ApplicationInstanceCondition;
import com.suixingpay.config.server.dto.VersionInfo;
import com.suixingpay.config.server.entity.ApplicationConfigDO;
import com.suixingpay.config.server.entity.ApplicationInstanceDO;
import com.suixingpay.config.server.entity.GlobalConfigDO;
import com.suixingpay.config.server.exception.ConfigException;
import com.suixingpay.config.server.form.UpdateApplicationInstanceForm;
import com.suixingpay.config.server.service.ApplicationConfigService;
import com.suixingpay.config.server.service.ApplicationInstanceService;
import com.suixingpay.config.server.service.GlobalConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: tangqihua[tang_qh@suixingpay.com]
 * @date: 2018年09月11日 15时35分
 * @version: V1.0
 * @review: tangqihua[tang_qh@suixingpay.com]/2018年09月11日 15时35分
 */
@Slf4j
@Validated
@Api(description = "应用实例信息")
@RestController
@RequestMapping("/applicationinstance")
public class ApplicationInstanceController {

    @Autowired
    private ApplicationConfigService applicationConfigService;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private ApplicationInstanceService instanceService;

    private static final int UNKNOWN_VERSION = -1;

    private static final int READ_TIMEOUT = 1000;

    @PostMapping
    @ApiOperation(value = "修改实例refresh用户名和密码", notes = "修改实例refresh用户名和密码")
    public void update(@RequestBody @Valid UpdateApplicationInstanceForm form) {
        instanceService.update(form.convertToApplicationInstanceDO());
    }

    @GetMapping
    @ApiOperation(value = "实例信息列表", notes = "实例信息列表")
    public List<ApplicationInstanceDO> list(@Valid ApplicationInstanceCondition condition) {
        return instanceService.list(condition);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除实例信息", notes = "删除实例信息")
    public void delete(@PathVariable("id") Long id) {
        instanceService.delete(id);
    }

    private String buildUrl(ApplicationInstanceDO instanceDO) {
        String url = "http://" + instanceDO.getIp() + ":" + instanceDO.getPort();
        String path = instanceDO.getManagerPath();
        if (null != path && !path.trim().isEmpty() && !"/".equals(path)) {
            url += path;
        }
        return url;
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "刷新最新实例配置", notes = "刷新最新实例配置")
    public VersionDTO refresh(@PathVariable Long id) throws ConfigException {
        try {
            ApplicationInstanceDO instanceDO = instanceService.findById(id);
            String url = buildUrl(instanceDO);
            url += "/refresh";
            RestTemplate restTemplate = genRestTemplate(instanceDO.getUsername(), instanceDO.getPassword());
            String res = restTemplate.postForObject(url, null, String.class);
            if (log.isDebugEnabled()) {
                log.debug(res);
            }
            return getNewVersion(instanceDO);
        } catch (ResourceAccessException e) {
            throw new ConfigException("刷新异常: 项目刷新端点不通");
        } catch (HttpClientErrorException e) {
            if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode())) {
                throw new ConfigException("刷新异常: 用户名或密码错误");
            } else {
                throw new ConfigException("刷新异常: {}", e.getStatusCode().toString());
            }

        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ConfigException("刷新异常:" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获取实例版本信息", notes = "获取实例版本信息")
    public VersionInfo getVersionInfo(@PathVariable("id") Long id) throws ConfigException {
        VersionDTO version = null;
        VersionDTO localVersion = null;
        try {
            ApplicationInstanceDO instanceDO = instanceService.findById(id);
            version = getNewVersion(instanceDO);
            String url = buildUrl(instanceDO);
            url += "/" + Constant.GET_CONFIG_VERSION_PATH;
            RestTemplate restTemplate = genRestTemplate(instanceDO.getUsername(), instanceDO.getPassword());
            localVersion = restTemplate.getForObject(url, VersionDTO.class);
        } catch (HttpClientErrorException e) {
            if (HttpStatus.UNAUTHORIZED.equals(e.getStatusCode())) {
                throw new ConfigException("获取版本异常: 用户名或密码错误");
            } else {
                throw new ConfigException("获取版本异常: {}", e.getStatusCode().toString());
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
            throw new ConfigException("获取版本异常: " + e.getMessage());
        }
        VersionInfo versionInfo = new VersionInfo();
        versionInfo.setLocalVersion(localVersion);
        versionInfo.setVersion(version);
        return versionInfo;
    }

    private VersionDTO getNewVersion(ApplicationInstanceDO instanceDO) {
        String profile = instanceDO.getProfile();
        String applicationName = instanceDO.getApplicationName();
        GlobalConfigDO globalConfigDO = globalConfigService.getByProfileForOpenApi(profile);
        ApplicationConfigDO applicationConfigDO = applicationConfigService.getForOpenApi(applicationName, profile);
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


    private RestTemplate genRestTemplate(String username, String password) {
        RestTemplate restTemplate = getRawRestTemplate();
        if (null != username && !username.trim().isEmpty()) {
            restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
        }
        return restTemplate;

    }

    private RestTemplate getRawRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(READ_TIMEOUT);
        requestFactory.setConnectTimeout(1000);
        return new RestTemplate(requestFactory);
    }

}
