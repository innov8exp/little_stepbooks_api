package net.stepbooks.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.stepbooks.infrastructure.exception.JsonException;

import java.util.List;

@Slf4j
public class JsonUtils {

    public static <T> T fromJson(String jsonStr, Class<T> clazz) throws JsonException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fromJson failed, json = {}, clazz = {}, exception = {}", jsonStr, clazz, e.getMessage());
            throw new JsonException(jsonStr, clazz);
        }
    }

    public static <T> List<T> fromJsonArray(String jsonStr, Class<T> clazz) throws JsonException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(jsonStr, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.error("fromJson failed, json = {}, list of clazz = {}", jsonStr, clazz, e);
            throw new JsonException(jsonStr, clazz);
        }
    }

    public static String toJson(Object object) throws JsonException {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("toJson failed, object = {}.", object, e);
            throw new JsonException(object);
        }
    }

    public static <O, I> O fromJson(String jsonStr, Class<O> outerClass, Class<I> innerClass) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            JavaType type = mapper.getTypeFactory().constructParametricType(outerClass, innerClass);
            return mapper.readValue(jsonStr, type);
        } catch (Exception e) {
            log.error("fromJson failed, json = {}, outClazz = {}, innerClazz = {}", jsonStr, outerClass, innerClass, e);
            throw new JsonException(jsonStr, outerClass, innerClass);
        }
    }

    public static <T> T fromJson(String jsonStr, TypeReference<T> clazz) throws JsonException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(jsonStr, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("fromJson failed, json = {}, clazz = {}, exception = {}", jsonStr, clazz, e.getMessage());
            throw new JsonException(jsonStr, clazz.getClass());
        }
    }
}
