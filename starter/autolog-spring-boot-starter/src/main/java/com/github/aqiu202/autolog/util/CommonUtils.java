package com.github.aqiu202.autolog.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * <pre>通用工具类</pre>
 * @author aqiu 2018年3月24日 下午2:38:08
 */
public class CommonUtils {

    private final static Logger log = LoggerFactory.getLogger(CommonUtils.class);

    /**
     * 占位符前缀: "{"
     */
    public static final String PLACEHOLDER_PREFIX = "{";
    /**
     * 占位符的后缀: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";

    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        Map<String, Object> result = new HashMap<>();
        try {
            Class<?> c = obj.getClass();
            Field[] fields = c.getDeclaredFields();
            fullMap(result, fields, obj);
            Class<?> sup = c.getSuperclass();
            while (sup != null && !sup.equals(Object.class)) {
                fullMap(result, sup.getDeclaredFields(), obj);
                sup = sup.getSuperclass();
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return result;
    }

    private static void fullMap(Map<String, Object> target, Field[] fields, Object obj)
            throws Exception {
        for (Field field : fields) {
            String key = field.getName();
            // 去掉static修饰的字段
            int m = field.getModifiers();
            if (Modifier.isStatic(m) || Modifier.isFinal(m)) {
                continue;
            }
            field.setAccessible(true); //NOSONAR
            Object value = field.get(obj);
            if (value == null) {
                continue;
            }
            target.put(key, value);
        }
    }

    /**
     * 占位符替换
     *
     * @param text 字符串
     * @param parameter 替换参数
     * @return 替换后的字符串
     */
    public static String stringFormat(String text, Map<String, Object> parameter) {
        return stringFormat(text, parameter, "");
    }

    /**
     * 占位符替换
     *
     * @param text 字符串
     * @param parameter 替换参数
     * @param nullValueReplacement 空值的默认值
     * @return 替换后的字符串
     */
    public static String stringFormat(String text, Map<String, Object> parameter,
            String nullValueReplacement) {
        if (StringUtils.isEmpty(text) || parameter == null || parameter.isEmpty()) {
            return replaceNullParam(text, nullValueReplacement);
        }
        StringBuilder buf = new StringBuilder(text);
        int startIndex = buf.indexOf(PLACEHOLDER_PREFIX);
        while (startIndex != -1) {
            int endIndex = buf
                    .indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
            if (endIndex != -1) {
                String placeholder = buf
                        .substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                try {
                    String propVal = parameter.get(placeholder) == null ? "null"
                            : parameter.get(placeholder).toString();
                    if (propVal != null) {
                        buf.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), propVal);
                        nextIndex = startIndex + propVal.length();
                    }
                } catch (Exception ex) {
                    log.error("", ex);
                }
                startIndex = buf.indexOf(PLACEHOLDER_PREFIX, nextIndex);
            } else {
                startIndex = -1;
            }
        }
        return replaceNullParam(buf.toString(), nullValueReplacement);
    }

    private static String replaceNullParam(String str, String replacement) {
        return str.replaceAll("\\{\\w*}", replacement); //NOSONAR
    }

}
