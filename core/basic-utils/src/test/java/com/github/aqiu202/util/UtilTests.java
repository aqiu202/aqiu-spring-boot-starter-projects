package com.github.aqiu202.util;

import com.github.aqiu202.util.retry.RetrievableCallable;
import com.github.aqiu202.util.retry.RetrievableRunnable;
import com.github.aqiu202.util.retry.RetrievableTask;
import com.github.aqiu202.util.scan.FileScanner;
import com.github.aqiu202.util.scan.ResourceScanner;
import com.github.aqiu202.util.scan.ScanResource;
import com.github.aqiu202.util.wrap.BooleanWrapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.junit.jupiter.api.Assertions.*;

public class UtilTests {

    @Test
    public void testScanner() {
        ResourceScanner springScanner = new FileScanner();
        ResourceScanner scanner = new FileScanner();
        List<ScanResource> scanResources1 = scanner.scanWithPackageName("com.github.aqiu202.util");
        List<ScanResource> scanResources2 = springScanner.scanWithPackageName("com.github.aqiu202.util");
        System.out.println(scanResources1);
        System.out.println(scanResources2);

    }

    @Test
    public void testJsr305AnnotationsAreAvailable() throws NoSuchMethodException {
        assertNotNull(ClassUtils.class.getMethod("isNumber", Class.class)
                .getParameters()[0].getAnnotation(Nonnull.class));
        assertNotNull(ClassUtils.class.getMethod("isNumber", Object.class)
                .getParameters()[0].getAnnotation(Nullable.class));
        assertNotNull(ReflectionUtils.class.getMethod("findClass", String.class)
                .getAnnotation(Nullable.class));
        assertNotNull(ReflectionUtils.class.getMethod("findClass", String.class)
                .getParameters()[0].getAnnotation(Nonnull.class));
        assertNotNull(FileScanner.class.getMethod("setClassLoader", ClassLoader.class)
                .getParameters()[0].getAnnotation(Nonnull.class));
        assertNotNull(RetrievableCallable.class.getMethod("getCallable").getAnnotation(Nonnull.class));
        assertNotNull(RetrievableRunnable.class.getMethod("getRunnable").getAnnotation(Nonnull.class));
        assertNotNull(RetrievableTask.class.getMethod("getRunnable").getAnnotation(Nonnull.class));
        assertNotNull(BooleanWrapper.class.getMethod("set", Boolean.class)
                .getParameters()[0].getAnnotation(Nonnull.class));
    }

    @Test
    public void testJacksonGenericRoundTrip() {
        Map<String, List<String>> source = new LinkedHashMap<>();
        source.put("名称", Arrays.asList("测试", "Java 8"));
        TypeReference<Map<String, List<String>>> type = new TypeReference<Map<String, List<String>>>() {};
        String json = JacksonUtils.toJson(source);
        byte[] bytes = JacksonUtils.toBytes(source);

        assertEquals(json, new String(bytes, StandardCharsets.UTF_8));
        assertEquals(source, JacksonUtils.toObject(json, type));
        assertEquals(source, JacksonUtils.toObject(bytes, type));
        assertEquals(source, JacksonUtils.convert(source, type));
    }

    @Test
    public void testJacksonClassRoundTrip() {
        String source = "中文内容";
        assertEquals(source, JacksonUtils.toObject(JacksonUtils.toJson(source), String.class));
        assertEquals(source, JacksonUtils.toObject(JacksonUtils.toBytes(source), String.class));
        assertEquals("null", JacksonUtils.toJson(null));
        assertNull(JacksonUtils.toObject("null", String.class));
    }

    @Test
    public void testJacksonJavaTimeRoundTrip() {
        DateValues source = new DateValues();
        source.date = LocalDate.of(2026, 10, 27);
        source.time = LocalTime.of(12, 34, 56);
        source.dateTime = LocalDateTime.of(source.date, source.time);
        source.instant = Instant.parse("2026-10-27T12:34:56Z");
        String json = JacksonUtils.toJson(source);

        assertTrue(json.contains("\"date\":\"2026-10-27\""));
        assertTrue(json.contains("\"time\":\"12:34:56\""));
        assertTrue(json.contains("\"dateTime\":\"2026-10-27 12:34:56\""));
        assertTrue(json.contains("\"instant\":\"2026-10-27T12:34:56Z\""));
        DateValues actual = JacksonUtils.toObject(json, DateValues.class);
        assertEquals(source.date, actual.date);
        assertEquals(source.time, actual.time);
        assertEquals(source.dateTime, actual.dateTime);
        assertEquals(source.instant, actual.instant);
    }

    @Test
    public void testJacksonCustomDateFormats() throws IOException {
        JacksonUtils.createObjectMapper();
        JacksonUtils.DateFormatters formats = JacksonUtils.DateFormatters.INSTANCE;
        String originalDate = formats.getDate();
        String originalTime = formats.getTime();
        String originalDateTime = formats.getDateTime();
        try {
            formats.setDate("yyyy/MM/dd");
            formats.setTime("HH-mm-ss");
            formats.setDateTime("yyyy/MM/dd HH-mm-ss");
            ObjectMapper mapper = JacksonUtils.createObjectMapper();
            DateValues source = new DateValues();
            source.date = LocalDate.of(2026, 10, 27);
            source.time = LocalTime.of(12, 34, 56);
            source.dateTime = LocalDateTime.of(source.date, source.time);
            String json = mapper.writeValueAsString(source);

            assertTrue(json.contains("\"date\":\"2026/10/27\""));
            assertTrue(json.contains("\"time\":\"12-34-56\""));
            assertTrue(json.contains("\"dateTime\":\"2026/10/27 12-34-56\""));
            DateValues actual = mapper.readValue(json, DateValues.class);
            assertEquals(source.date, actual.date);
            assertEquals(source.time, actual.time);
            assertEquals(source.dateTime, actual.dateTime);
        } finally {
            formats.setDate(originalDate);
            formats.setTime(originalTime);
            formats.setDateTime(originalDateTime);
        }
    }

    @Test
    public void testJacksonLenientMappingAndConversion() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("value", 42);
        PrimitiveValue converted = JacksonUtils.convert(source, PrimitiveValue.class);
        assertEquals(42, converted.value);
        PrimitiveValue actual = JacksonUtils.toObject("{\"value\":null,\"unknown\":true}", PrimitiveValue.class);
        assertEquals(0, actual.value);
        assertEquals("{}", JacksonUtils.toJson(new Object()));
        assertNull(JacksonUtils.convert(null, PrimitiveValue.class));
        assertNull(JacksonUtils.convert(null, new TypeReference<List<String>>() {}));
    }

    @Test
    public void testJacksonExceptionsAreWrapped() {
        RuntimeException classException = assertThrows(RuntimeException.class,
                () -> JacksonUtils.toObject("{", PrimitiveValue.class));
        assertEquals("Json反序列化异常", classException.getMessage());
        assertTrue(classException.getCause() instanceof IOException);
        RuntimeException genericException = assertThrows(RuntimeException.class,
                () -> JacksonUtils.toObject("{", new TypeReference<Map<String, Object>>() {}));
        assertEquals("Json反序列化异常", genericException.getMessage());
        assertTrue(genericException.getCause() instanceof IOException);
        RuntimeException serializationException = assertThrows(RuntimeException.class,
                () -> JacksonUtils.toJson(new FailingValue()));
        assertEquals("Json序列化异常", serializationException.getMessage());
        assertTrue(serializationException.getCause() instanceof IOException);
    }

    public static class DateValues {
        public LocalDate date;
        public LocalTime time;
        public LocalDateTime dateTime;
        public Instant instant;
    }

    public static class PrimitiveValue {
        public int value;
    }

    public static class FailingValue {
        public String getValue() {
            throw new IllegalStateException("测试序列化异常");
        }
    }
}
