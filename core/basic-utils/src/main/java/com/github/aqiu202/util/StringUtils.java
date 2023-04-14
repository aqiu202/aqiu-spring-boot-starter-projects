package com.github.aqiu202.util;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>{@link StringUtils}</pre>
 *
 * @author aqiu 2020/12/13 0:02
 **/
public abstract class StringUtils {

    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    /**
     * 占位符前缀: "{"
     */
    public static final String PLACEHOLDER_PREFIX = "{";
    /**
     * 占位符的后缀: "}"
     */
    public static final String PLACEHOLDER_SUFFIX = "}";

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean notEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean hasText(CharSequence str) {
        return notEmpty(str) && containsText(str);
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 占位符替换
     *
     * @param text      字符串
     * @param parameter 替换参数
     * @return 替换后的字符串
     */
    public static String stringFormat(String text, Map<String, Object> parameter) {
        return stringFormatWithNull(text, parameter, "");
    }

    /**
     * 占位符替换
     *
     * @param text      字符串
     * @param parameter 替换参数
     * @return 替换后的字符串
     */
    public static String stringFormat(String text, Object... parameter) {
        Map<Integer, Object> map = new HashMap<>();
        for (int i = 0; i < parameter.length; i++) {
            map.put(i + 1, parameter[i]);
        }
        return stringFormatWithNull(text, map, "");
    }

    /**
     * 占位符替换
     *
     * @param text                 字符串
     * @param parameter            替换参数
     * @param nullValueReplacement 空值的默认值
     * @return 替换后的字符串
     */
    public static String stringFormatWithNull(String text, Map<?, ?> parameter,
            String nullValueReplacement) {
        if (isEmpty(text) || CollectionUtils.isEmpty(parameter)) {
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
