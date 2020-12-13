package com.github.aqiu202.util;

/**
 * <pre>{@link PropertyNameUtils}</pre>
 *
 * @author aqiu 2020/12/13 0:16
 **/
public abstract class PropertyNameUtils {

    private static final char underline = '_';

    /**
     * 驼峰转下划线命名
     *
     * @author AQIU 2018年5月16日 下午2:44:14
     * @param propName propName
     * @return 转换后的字段名称
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
     * @return 转换后的字段名称
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

    public static String capitalize(String str) {
        return changeFirstCharacterCase(str, true);
    }

    public static String uncapitalize(String str) {
        return changeFirstCharacterCase(str, false);
    }

    private static String changeFirstCharacterCase(String str, boolean capitalize) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        char baseChar = str.charAt(0);
        char updatedChar;
        if (capitalize) {
            updatedChar = Character.toUpperCase(baseChar);
        }
        else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars, 0, chars.length);
    }
}
