package com.github.aqiu202.http.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import com.github.aqiu202.util.JacksonUtils;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class JacksonSerializer implements JsonSerializer{

    public static final JacksonSerializer INSTANCE = new JacksonSerializer();

    private final ObjectMapper objectMapper;

    public JacksonSerializer() {
            this.objectMapper = new ObjectMapper();
            objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES,
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            objectMapper.registerModule(new JavaTimeModule());
            SimpleModule simpleModule = new SimpleModule();
            JacksonUtils.DateFormatters dateFormatters = JacksonUtils.DateFormatters.INSTANCE;
            List<com.fasterxml.jackson.databind.JsonSerializer> jsonSerializers = Arrays.asList(
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
            objectMapper.registerModules(simpleModule);
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
}
