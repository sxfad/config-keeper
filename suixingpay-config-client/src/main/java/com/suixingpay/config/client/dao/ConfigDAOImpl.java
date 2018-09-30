package com.suixingpay.config.client.dao;

import com.suixingpay.config.client.AddApplicationInstanceInfoInterceptor;
import com.suixingpay.config.client.SxfConfigClientProperties;
import com.suixingpay.config.client.util.JsonUtil;
import com.suixingpay.config.common.to.PropertySource;
import com.suixingpay.config.common.to.VersionDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.List;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月20日 下午3:37:23
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月20日 下午3:37:23
 */
@Slf4j
public class ConfigDAOImpl implements ConfigDAO {

    private static final int READ_TIMEOUT = 60000;

    private static final int SECOND = 1000;

    private final SxfConfigClientProperties configClientProperties;

    private String[] versionUrls;

    private String[] globalConfigUrls;

    private String[] applicationConfigUrls;

    private int index = 0;

    private String globalConfigCacheFile;

    private String applicationConfigCacheFile;

    // refresh 后 ConfigDAOImpl 在Spring 容器中不是单例，需要使用static
    private static PropertySource globalConfigCache;

    private static PropertySource applicationConfigCache;

    private RestTemplate restTemplate;

    public ConfigDAOImpl(SxfConfigClientProperties configClientProperties) {
        this.configClientProperties = configClientProperties;
        init();
    }

    private void init() {
        List<String> uris = configClientProperties.getUris();
        if (null == uris || uris.isEmpty()) {
            throw new RuntimeException("没有设置suixingpay.config.uris");
        }
        int size = uris.size();
        versionUrls = new String[size];
        globalConfigUrls = new String[size];
        applicationConfigUrls = new String[size];
        String separator = "/";
        for (int i = 0; i < size; i++) {
            String uri = uris.get(i);
            if (!uri.endsWith(separator)) {
                uri += separator;
            }
            versionUrls[i] = uri + "open/version?application={application}&profile={profile}";
            globalConfigUrls[i] = uri + "open/{profile}?version={version}";
            applicationConfigUrls[i] = uri + "open/{application}/{profile}?version={version}";
        }
        String cachePath = configClientProperties.getCachePath();
        if (null != cachePath && cachePath.trim().length() > 0) {
            if (!cachePath.endsWith(separator)) {
                cachePath += separator;
            }
            this.globalConfigCacheFile = cachePath + "global-config.cache";
            this.applicationConfigCacheFile = cachePath + getName() + "-" + getProfile() + ".cache";
        }
    }

    private String getProfile() {
        return configClientProperties.getProfile();
    }

    private String getName() {
        return configClientProperties.getName();
    }

    public int getIndex() {
        if (index > 10000) {
            index = 0;
        } else {
            index++;
        }
        return index;
    }

    @Override
    public VersionDTO getVersion() {
        genRestTemplate();
        VersionDTO versionTO = null;
        int size = versionUrls.length;
        String versionUrl = versionUrls[getIndex() % size];
        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(versionUrl, String.class, getName(),
                    getProfile());
            if (null != responseEntity && responseEntity.hasBody()) {
                String json = responseEntity.getBody();
                versionTO = JsonUtil.jsonToObject(json, VersionDTO.class);
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
        return versionTO;
    }

    @Override
    public PropertySource getGlobalConfig() {
        genRestTemplate();
        PropertySource propertySource = null;
        PropertySource cache = getGlobalConfigLocalCache();
        Integer version = null;
        if (null != cache) {
            version = cache.getVersion();
        }
        int size = globalConfigUrls.length;
        String globalConfigUrl = globalConfigUrls[getIndex() % size];
        try {
            log.debug("loading {} global config, url:", getProfile(), globalConfigUrl);
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(globalConfigUrl, String.class,
                    getProfile(), version);
            if (null != responseEntity) {
                // 如果没有修改返回本地缓存
                if (responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
                    return cache;
                }
                if (responseEntity.hasBody()) {
                    String json = responseEntity.getBody();
                    propertySource = JsonUtil.jsonToObject(json, PropertySource.class);
                    if (null != propertySource) {
                        this.globalConfigCache = propertySource;
                        writeFile(this.globalConfigCacheFile, json);
                    }
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return propertySource;
    }

    @Override
    public PropertySource getGlobalConfigLocalCache() {
        if (null != this.globalConfigCache) {
            return globalConfigCache;
        }
        String json = readFile(this.globalConfigCacheFile);
        if (null != json && json.trim().length() > 0) {
            this.globalConfigCache = JsonUtil.jsonToObject(json, PropertySource.class);
        }
        return this.globalConfigCache;
    }

    @Override
    public PropertySource getApplicationConfig() {
        genRestTemplate();
        PropertySource propertySource = null;
        PropertySource cache = getApplicationConfigLocalCache();
        Integer version = null;
        if (null != cache) {
            version = cache.getVersion();
        }
        int size = applicationConfigUrls.length;
        String applicationConfigUrl = applicationConfigUrls[getIndex() % size];
        try {
            log.debug("load application:{}, profile:{} config", getName(), getProfile());
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(applicationConfigUrl, String.class,
                    getName(), getProfile(), version);
            if (null != responseEntity) {
                // 如果没有修改返回本地缓存
                if (responseEntity.getStatusCode() == HttpStatus.NOT_MODIFIED) {
                    return cache;
                }
                if (responseEntity.hasBody()) {
                    String json = responseEntity.getBody();
                    propertySource = JsonUtil.jsonToObject(json, PropertySource.class);
                    if (null != propertySource) {
                        applicationConfigCache = propertySource;
                        writeFile(this.applicationConfigCacheFile, json);
                    }
                }
            }
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }

        return propertySource;
    }

    @Override
    public PropertySource getApplicationConfigLocalCache() {
        if (null != applicationConfigCache) {
            return applicationConfigCache;
        }
        String json = readFile(applicationConfigCacheFile);
        if (null != json && !json.trim().isEmpty()) {
            applicationConfigCache = JsonUtil.jsonToObject(json, PropertySource.class);
        }
        return applicationConfigCache;
    }

    private void genRestTemplate() {
        if (null == this.restTemplate) {
            this.restTemplate = getRawRestTemplate();
            String username = configClientProperties.getUsername();
            String password = configClientProperties.getPassword();
            if (null != username && !username.trim().isEmpty()) {
                this.restTemplate.getInterceptors().add(new BasicAuthorizationInterceptor(username, password));
            }
            this.restTemplate.getInterceptors().add(new AddApplicationInstanceInfoInterceptor(configClientProperties));
        }
    }

    private RestTemplate getRawRestTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(READ_TIMEOUT);
        requestFactory.setConnectTimeout(1000);
        return new RestTemplate(requestFactory);
    }

    private void writeFile(String path, String content) {
        if (null == path || path.length() == 0) {
            return;
        }
        File file = new File(path);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }

        try (OutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(content.getBytes(UTF8));
            outputStream.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private String readFile(String path) {
        if (null == path || path.length() == 0) {
            return null;
        }
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            return null;
        }
        int cacheTimeOut = configClientProperties.getCacheTimeOut();
        if (cacheTimeOut > 0 && (System.currentTimeMillis() - file.lastModified()) / SECOND > cacheTimeOut) {
            return null;
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            int len = 0;
            byte[] buf = new byte[1024];
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            while ((len = inputStream.read(buf)) != -1) {
                bStream.write(buf, 0, len);
            }
            return new String(bStream.toByteArray(), UTF8);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
