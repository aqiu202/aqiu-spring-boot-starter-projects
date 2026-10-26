package com.github.aqiu202.http.util;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalDateTimeDeserializer;
import tools.jackson.databind.ext.javatime.deser.LocalTimeDeserializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalDateTimeSerializer;
import tools.jackson.databind.ext.javatime.ser.LocalTimeSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class JacksonSerializer implements JsonSerializer {

    public static final JacksonSerializer INSTANCE = new JacksonSerializer();

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private final ObjectMapper objectMapper;

    public static JacksonSerializer create(ObjectMapper objectMapper) {
        return new JacksonSerializer(objectMapper);
    }

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JacksonSerializer() {
        SimpleModule simpleModule = new SimpleModule();
        List<ValueSerializer<?>> jsonSerializers = Arrays.asList(
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)),
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));

        List<ValueDeserializer<?>> jsonDeserializers = Arrays.asList(
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)),
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN))
        );
        jsonSerializers.forEach(js -> addSerializer(simpleModule, js));
        jsonDeserializers.forEach(jd -> addDeserializer(simpleModule, jd));
        this.objectMapper = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .addModule(simpleModule)
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addSerializer(SimpleModule module, ValueSerializer<?> serializer) {
        module.addSerializer((Class) serializer.handledType(), (ValueSerializer<?>) serializer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addDeserializer(SimpleModule module, ValueDeserializer<?> deserializer) {
        module.addDeserializer((Class) deserializer.handledType(), (ValueDeserializer) deserializer);
    }

    @Override
    public String serialize(Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof String) {
            return (String) object;
        }
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (JacksonException e) {
            throw new RuntimeException("json序列化异常：", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T deserialize(String json, Type type) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            return (T) this.objectMapper.readValue(json, this.objectMapper.getTypeFactory().constructType(type));
        } catch (JacksonException e) {
            throw new RuntimeException("json反序列化异常：", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap(Object bean) {
        if (bean == null) {
            return Collections.emptyMap();
        }
        return this.objectMapper.convertValue(bean, Map.class);
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }
}
