package com.github.aqiu202.util;

import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
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

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JacksonUtils {

    private static final ObjectMapper MAPPER = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        DateFormatters dateFormatters = DateFormatters.INSTANCE;
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
        return JsonMapper.builder()
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                        DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
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

    public static byte[] toBytes(Object target) {
        try {
            return MAPPER.writeValueAsBytes(target);
        } catch (JacksonException e) {
            throw new RuntimeException("Json序列化异常", e);
        }
    }


    public static <T> T toObject(byte[] source, Class<T> targetType) {
        try {
            return MAPPER.readValue(source, targetType);
        } catch (JacksonException e) {
            throw new RuntimeException("Json反序列化异常", e);
        }
    }

    public static <T> T toObject(byte[] source, TypeReference<T> targetType) {
        try {
            return MAPPER.readValue(source, targetType);
        } catch (JacksonException e) {
            throw new RuntimeException("Json反序列化异常", e);
        }
    }

    public static String toJson(Object target) {
        return new String(toBytes(target), StandardCharsets.UTF_8);
    }


    public static <T> T toObject(String source, Class<T> targetType) {
        return toObject(source.getBytes(StandardCharsets.UTF_8), targetType);
    }

    public static <T> T toObject(String source, TypeReference<T> targetType) {
        return toObject(source.getBytes(StandardCharsets.UTF_8), targetType);
    }

    public static <T> T convert(Object object, Class<T> targetType) {
        if (object == null) {
            return null;
        }
        return MAPPER.convertValue(object, targetType);
    }

    public static <T> T convert(Object object, TypeReference<T> targetType) {
        if (object == null) {
            return null;
        }
        return MAPPER.convertValue(object, targetType);
    }

    public static class DateFormatters {

        public static final DateFormatters INSTANCE = new DateFormatters();

        private String date = "yyyy-MM-dd";

        private String time = "HH:mm:ss";

        private String dateTime = "yyyy-MM-dd HH:mm:ss";

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }
    }
}
