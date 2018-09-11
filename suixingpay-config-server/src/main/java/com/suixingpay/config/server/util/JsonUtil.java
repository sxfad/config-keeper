package com.suixingpay.config.server.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 生成JSON字符串
     *
     * @param obj
     * @return
     * @throws JsonProcessingException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String objectToJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成JSON字符串
     *
     * @param obj
     * @param serializationView
     * @return
     * @throws JsonProcessingException
     * @throws Exception
     */
    public static String objectToJson(Object obj, Class<?> serializationView) throws JsonProcessingException {
        if (null != serializationView) {
            return mapper.writerWithView(serializationView).writeValueAsString(obj);
        } else {
            return mapper.writeValueAsString(obj);
        }
    }

    /**
     * 将JSON字符串转化为 指定类型
     *
     * @param json json
     * @param c    期望类型
     * @return T 结果
     */
    public static <T> T jsonToObject(String json, Class<T> c) {
        T t = null;
        try {
            if (json != null && json.trim().length() > 0) {
                t = mapper.readValue(json, c);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * @param json
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String json) {
        return jsonToObject(json, new TypeReference<Map<String, T>>() {
        });
    }

    /**
     * 将JSON字符串转化为 指定类型
     *
     * @param json json
     * @param c    期望类型
     * @return T 结果
     */
    public static <T> T jsonToObject(String json, TypeReference<T> c) {
        T t = null;
        try {
            if (json != null && json.trim().length() > 0) {
                t = mapper.readValue(json, c);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    /**
     * 获取Json树
     *
     * @param json
     * @return
     */
    public static JsonNode toJsonTree(String json) {
        try {
            if (json != null && json.trim().length() > 0) {
                return mapper.readTree(json);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

}
