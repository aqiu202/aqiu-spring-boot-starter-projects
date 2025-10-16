package com.github.aqiu202.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>{@link StringUtils}</pre>
 *
 * @author aqiu 2020/12/13 0:02
 **/
public abstract class StringUtils {

    private static final Logger log = LoggerFactory.getLogger(StringUtils.class);

    private static final char UNION = '_';

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

    public static boolean hasText(CharSequence str) {
        return isNotEmpty(str) && containsText(str);
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(CharSequence str) {
        return isEmpty(str) || !containsText(str);
    }

    public static boolean isNotBlank(CharSequence str) {
        return !isBlank(str);
    }

    public static boolean equals(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            if (cs1.length() != cs2.length()) {
                return false;
            } else if (cs1 instanceof String && cs2 instanceof String) {
                return cs1.equals(cs2);
            } else {
                int length = cs1.length();
                for (int i = 0; i < length; ++i) {
                    if (cs1.charAt(i) != cs2.charAt(i)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean equalsAny(CharSequence string, CharSequence... searchStrings) {
        for (CharSequence next : searchStrings) {
            if (equals(string, next)) {
                return true;
            }
        }

        return false;
    }

    public static boolean equalsAnyIgnoreCase(CharSequence string, CharSequence... searchStrings) {
        for (CharSequence next : searchStrings) {
            if (equalsIgnoreCase(string, next)) {
                return true;
            }
        }
        return false;
    }

    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            if (cs1.length() != cs2.length()) {
                return false;
            }
            int thisStart = 0;
            int start = 0;
            int length = cs2.length();
            if (cs1 instanceof String && cs2 instanceof String) {
                return ((String) cs1).regionMatches(true, thisStart, (String) cs2, start, length);
            } else {
                int index1 = thisStart;
                int index2 = start;
                int tmpLen = length;
                int srcLen = cs1.length() - thisStart;
                int otherLen = cs2.length() - start;
                if (srcLen >= length && otherLen >= length) {
                    while (tmpLen-- > 0) {
                        char c1 = cs1.charAt(index1++);
                        char c2 = cs2.charAt(index2++);
                        if (c1 != c2) {
                            char u1 = Character.toUpperCase(c1);
                            char u2 = Character.toUpperCase(c2);
                            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                                return false;
                            }
                        }
                    }

                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
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
     * 驼峰法转下划线
     * @param name 字段
     * @return 下划线格式字段
     */
    public static String camelToUnderline(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        // 前置字符是否大写
        boolean preCharIsUpperCase = true;
        // 当前字符是否大写
        boolean curreCharIsUpperCase = true;
        // 下一字符是否大写
        boolean nexteCharIsUpperCase = true;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0) {
                preCharIsUpperCase = Character.isUpperCase(name.charAt(i - 1));
            } else {
                preCharIsUpperCase = false;
            }

            curreCharIsUpperCase = Character.isUpperCase(c);

            if (i < (name.length() - 1)) {
                nexteCharIsUpperCase = Character.isUpperCase(name.charAt(i + 1));
            }

            if (preCharIsUpperCase && curreCharIsUpperCase && !nexteCharIsUpperCase) {
                sb.append("_");
            } else if ((i != 0 && !preCharIsUpperCase) && curreCharIsUpperCase) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(c));
        }

        return sb.toString();
    }

    /**
     * 驼峰法转下划线然后大写
     * @param name 字段名称
     * @return 转换后的大写字段名称
     */
    public static String camelToUnderlineAndToUpperCase(String name) {
        return camelToUnderline(name).toUpperCase();
    }

    /**
     * 下划线转驼峰
     *
     * @param name 字段名称
     * @return 转换后的驼峰格式字段名称
     */
    public static String underlineToCamel(String name) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        int len = name.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = Character.toLowerCase(name.charAt(i));
            if (c == UNION) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(name.charAt(i)));
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
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
        } else {
            updatedChar = Character.toLowerCase(baseChar);
        }
        if (baseChar == updatedChar) {
            return str;
        }

        char[] chars = str.toCharArray();
        chars[0] = updatedChar;
        return new String(chars, 0, chars.length);
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
