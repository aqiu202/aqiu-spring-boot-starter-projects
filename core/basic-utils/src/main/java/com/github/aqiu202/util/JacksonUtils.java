package com.github.aqiu202.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JacksonUtils {

    private static final ObjectMapper MAPPER = createObjectMapper();

    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.registerModule(new JavaTimeModule());
        SimpleModule simpleModule = new SimpleModule();
        DateFormatters dateFormatters = DateFormatters.INSTANCE;
        List<JsonSerializer> jsonSerializers = Arrays.asList(
                new LocalDateSerializer(DateTimeFormatter.ofPattern(dateFormatters.getDate())),
                new LocalTimeSerializer(DateTimeFormatter.ofPattern(dateFormatters.getTime())),
                new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateFormatters.getDateTime())));

        List<JsonDeserializer> jsonDeserializers = Arrays.asList(
                new LocalDateDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getDate())),
                new LocalTimeDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getTime())),
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(dateFormatters.getDateTime()))
        );
        jsonSerializers.forEach(js -> simpleModule.addSerializer(js.handledType(), js));
        jsonDeserializers.forEach(jd -> simpleModule.addDeserializer(jd.handledType(), jd));
        return objectMapper.registerModules(simpleModule);
    }

    public static byte[] toBytes(Object target) {
        try {
            return MAPPER.writeValueAsBytes(target);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json序列化异常", e);
        }
    }


    public static <T> T toObject(byte[] source, Class<T> targetType) {
        try {
            return MAPPER.readValue(source, targetType);
        } catch (IOException e) {
            throw new RuntimeException("Json反序列化异常", e);
        }
    }

    public static <T> T toObject(byte[] source, TypeReference<T> targetType) {
        try {
            return MAPPER.readValue(source, targetType);
        } catch (IOException e) {
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
