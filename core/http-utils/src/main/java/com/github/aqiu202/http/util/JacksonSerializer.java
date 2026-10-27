package com.github.aqiu202.http.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JacksonSerializer implements com.github.aqiu202.http.util.JsonSerializer {

    private static final String DATE_PATTERN = "yyyy-MM-dd";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 反序列化时同时兼容 {@code yyyy-MM-dd HH:mm:ss} 与 ISO-8601（{@code yyyy-MM-dd'T'HH:mm:ss[.SSS]}）格式。
     * 须声明在 {@link #INSTANCE} 之前，否则初始化 INSTANCE 时该字段尚为 null。
     */
    private static final DateTimeFormatter LENIENT_DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ISO_LOCAL_DATE)
            .optionalStart().appendLiteral('T').optionalEnd()
            .optionalStart().appendLiteral(' ').optionalEnd()
            .append(DateTimeFormatter.ISO_LOCAL_TIME)
            .toFormatter();

    public static final JacksonSerializer INSTANCE = new JacksonSerializer();

    private final ObjectMapper objectMapper;

    public static JacksonSerializer create(ObjectMapper objectMapper) {
        return new JacksonSerializer(objectMapper);
    }

    public JacksonSerializer(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "objectMapper must not be null");
    }

    public JacksonSerializer() {
        SimpleModule simpleModule = new SimpleModule();
        List<com.fasterxml.jackson.databind.JsonSerializer<?>> jsonSerializers = Arrays.asList(
                new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_PATTERN)),
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(TIME_PATTERN)),
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));

        List<JsonDeserializer<?>> jsonDeserializers = Arrays.asList(
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(DATE_PATTERN)),
                new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME),
                new LocalDateTimeDeserializer(LENIENT_DATE_TIME_FORMATTER)
        );
        jsonSerializers.forEach(js -> addSerializer(simpleModule, js));
        jsonDeserializers.forEach(jd -> addDeserializer(simpleModule, jd));
        this.objectMapper = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS,
                        SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
                .addModule(new JavaTimeModule())
                .addModule(simpleModule)
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addSerializer(SimpleModule module, com.fasterxml.jackson.databind.JsonSerializer<?> serializer) {
        module.addSerializer((Class) serializer.handledType(),
                (com.fasterxml.jackson.databind.JsonSerializer) serializer);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addDeserializer(SimpleModule module, JsonDeserializer<?> deserializer) {
        module.addDeserializer((Class) deserializer.handledType(), (JsonDeserializer) deserializer);
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
        } catch (JsonProcessingException e) {
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
        } catch (JsonProcessingException e) {
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
