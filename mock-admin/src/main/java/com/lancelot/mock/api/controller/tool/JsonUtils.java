package com.lancelot.mock.api.controller.tool;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author liulei1971
 * @date 2023/5/12 13:55
 */
public class JsonUtils {

    private static Logger log = LoggerFactory.getLogger(JsonUtils.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    public JsonUtils() {
    }

    public static void init(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    public static String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var2) {
            return "";
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isBlank(text)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(text, clazz);
            } catch (IOException var3) {
                log.error("json parse err,json:{}", text, var3);
                throw new RuntimeException(var3);
            }
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(text)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(text, typeReference);
            } catch (IOException var3) {
                log.error("json parse err,json:{}", text, var3);
                throw new RuntimeException(var3);
            }
        }
    }

    public static <T> T parseObject(String text, JavaType javaType) {
        if (StringUtils.isEmpty(text)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(text, javaType);
            } catch (IOException var3) {
                log.error("json parse err,json:{}", text, var3);
                throw new RuntimeException(var3);
            }
        }
    }

    public static <T> T parseObject(JsonNode jsonNode, Class<T> clazz) {
        if (jsonNode != null && !jsonNode.isEmpty()) {
            try {
                return objectMapper.treeToValue(jsonNode, clazz);
            } catch (IOException var3) {
                log.error("JsonNode parse err,json:{}", jsonNode, var3);
                throw new RuntimeException(var3);
            }
        } else {
            return null;
        }
    }

    public static Object parseObject(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        } else {
            try {
                return objectMapper.readValue(text, Object.class);
            } catch (IOException var2) {
                log.error("json parse err,json:{}", text, var2);
                throw new RuntimeException(var2);
            }
        }
    }

    public static JsonNode parseTree(String text) {
        try {
            return objectMapper.readTree(text);
        } catch (Exception var2) {
            log.error("json parse err,json:{}", text, var2);
            throw new RuntimeException(var2);
        }
    }

    public static JsonNode parseTree(byte[] text) {
        try {
            return objectMapper.readTree(text);
        } catch (IOException var2) {
            log.error("json parse err,json:{}", text, var2);
            throw new RuntimeException(var2);
        }
    }

    public static <K, V> Map<K, V> convertToMap(String text) {
        try {
            return (Map)objectMapper.readValue(text, Map.class);
        } catch (IOException var2) {
            log.error("json convertToMap err,json:{}", text, var2);
            throw new RuntimeException(var2);
        }
    }

    public static boolean isJson(String text) {
        try {
            objectMapper.readTree(text);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.registerModule(new JavaTimeModule());
    }
}
