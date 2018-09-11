package com.suixingpay.config.common.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author: qiujiayu[qiu_jy@suixingpay.com]
 * @date: 2017年9月12日 下午4:38:04
 * @version: V1.0
 * @review: qiujiayu[qiu_jy@suixingpay.com]/2017年9月12日 下午4:38:04
 */
public class PropertySource {

    private String name;

    private int version = -1;

    private Map<String, Object> source;

    @JsonCreator
    public PropertySource(@JsonProperty("name") String name, @JsonProperty("version") int version,
                          @JsonProperty("source") Map<String, Object> source) {
        this.name = name;
        this.version = version;
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public int getVersion() {
        return version;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "PropertySource [name=" + name + ", version=" + version + "]";
    }

}
