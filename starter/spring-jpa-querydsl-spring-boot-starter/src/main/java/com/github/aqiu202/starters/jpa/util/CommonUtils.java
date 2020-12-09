package com.github.aqiu202.starters.jpa.util;

import com.google.common.primitives.Primitives;
import java.util.Collection;
import java.util.Map;
import org.springframework.util.StringUtils;

public final class CommonUtils {

    private static final char underline = '_';

    /**
     * 驼峰转下划线命名
     *
     * @author AQIU 2018年5月16日 下午2:44:14
     * @param propName propName
     */
    public static String underline(String propName) {
        if (StringUtils.hasText(propName)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < propName.length(); i++) {
                char c = propName.charAt(i);
                if (i > 0 && Character.isUpperCase(c)) {
                    sb.append(underline).append(Character.toLowerCase(c));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return propName;
        }
    }

    /**
     * 下划线命名转驼峰
     *
     * @author AQIU 2018年5月16日 下午2:43:49
     * @param propName propName
     */
    public static String hump(String propName) {
        if (StringUtils.hasText(propName)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < propName.length(); i++) {
                char c = propName.charAt(i);
                if (c == underline) {
                    sb.append(Character.toUpperCase(propName.charAt(++i)));
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        } else {
            return propName;
        }
    }

    private static Class<?> normalize(Class<?> cl) {
        return cl.isPrimitive() ? Primitives.wrap(cl) : cl;
    }

    public static boolean notAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return !normalize(cl1).isAssignableFrom(normalize(cl2));
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean notEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean notEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean notEmpty(Object[] array) {
        return !isEmpty(array);
    }

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

}
