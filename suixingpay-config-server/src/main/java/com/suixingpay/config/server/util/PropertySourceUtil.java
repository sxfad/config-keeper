package com.suixingpay.config.server.util;

import com.suixingpay.config.common.io.StringResource;
import com.suixingpay.config.server.entity.BasePropertySourceDO;
import com.suixingpay.config.server.enums.SourceType;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月14日 下午2:55:09
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月14日 下午2:55:09
 */
public class PropertySourceUtil {

    private static final String UTF_8 = "UTF-8";

    public static Map<String, Object> toPropertySource(BasePropertySourceDO propertySourceDO) throws Throwable {
        return toPropertySource(propertySourceDO.getPropertySource(), propertySourceDO.getSourceType());
    }

    /**
     * @param content
     * @param sourceType
     * @return
     * @throws Throwable
     */
    public static Map<String, Object> toPropertySource(String content, SourceType sourceType) throws Throwable {
        Properties source = null;
        if (sourceType == SourceType.PROPERTIES) {
            source = toProperties(content);
        } else if (sourceType == SourceType.YAML) {
            source = toYaml(content);
        }
        Map<String, Object> res = new HashMap<>(source.size());
        for (Enumeration<?> e = source.keys(); e.hasMoreElements(); ) {
            Object k = e.nextElement();
            Object v = source.get(k);
            if (k instanceof String) {
                res.put((String) k, v);
            }
        }
        return res;
    }

    /**
     * @param content
     * @return
     * @throws Throwable
     */
    public static Properties toProperties(String content) throws Throwable {
        Properties properties = new Properties();
        try (InputStream inputStream = new ByteArrayInputStream(content.getBytes(UTF_8));) {
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, UTF_8));
            properties.load(bf);
        }
        return properties;
    }

    /**
     * @param content
     * @return
     * @throws Throwable
     */
    public static Properties toYaml(String content) throws Throwable {
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new StringResource(content, "UTF-8"));
        return yaml.getObject();
    }
}
