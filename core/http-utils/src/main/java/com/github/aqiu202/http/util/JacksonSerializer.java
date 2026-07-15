package com.github.aqiu202.http.util;

import com.github.aqiu202.util.JacksonUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.ValueDeserializer;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.datatype.jsr310.JavaTimeModule;
import tools.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import tools.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import tools.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import tools.jackson.datatype.jsr310.ser.LocalDateSerializer;
import tools.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import tools.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JacksonSerializer implements JsonSerializer {

    public static final JacksonSerializer INSTANCE = new JacksonSerializer();

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
        JacksonUtils.DateFormatters dateFormatters = JacksonUtils.DateFormatters.INSTANCE;
        SimpleModule simpleModule = new SimpleModule();
        List<ValueSerializer<?>> jsonSerializers = Arrays.asList(
                new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormatters.getDate())),
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(dateFormatters.getTime())),
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormatters.getDateTime())));

        List<ValueDeserializer<?>> jsonDeserializers = Arrays.asList(
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getDate())),
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getTime())),
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getDateTime()))
        );
        jsonSerializers.forEach(js -> addSerializer(simpleModule, js));
        jsonDeserializers.forEach(jd -> addDeserializer(simpleModule, jd));
        this.objectMapper = JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .addModule(new JavaTimeModule())
                .addModule(simpleModule)
                .build();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void addSerializer(SimpleModule module, ValueSerializer<?> serializer) {
        module.addSerializer((Class) serializer.handledType(), (ValueSerializer) serializer);
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
}
