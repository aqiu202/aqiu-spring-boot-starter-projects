package com.github.aqiu202.util;

import java.util.Arrays;

public final class RandomCodeUtils {

    private RandomCodeUtils() {
    }

    // 去掉了不容易辨认的0(零)和o(大、小写)
    private final static char[] codes = {'1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j',
            'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C',
            'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};

    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyz";

    public enum RandomCodeLevel {
        Simple, Medium, Hard;

        RandomCodeLevel() {
        }
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateCode(int length, boolean repeat) {
        char[] chars = new char[length];
        if (repeat) {
            for (int i = 0; i < length; i++) {
                chars[i] = SYMBOLS.charAt(RandomUtils.nextInt(SYMBOLS.length()));
            }
        } else {
            char[] _chars = SYMBOLS.toCharArray();
            for (int i = 0; i < length; i++) {
                int index = RandomUtils.nextInt(_chars.length);
                chars[i] = _chars[index];
                _chars = removeItem(_chars, index);
            }
        }
        return new String(chars);
    }


    public static String generateSecurityCode() {
        return generateSecurityCode(4, RandomCodeLevel.Simple, false);
    }

    public static String generateSecurityCode(RandomCodeLevel level) {
        return generateSecurityCode(4, level, true);
    }

    public static String generateSecurityCode(int length) {
        return generateSecurityCode(length, RandomCodeLevel.Simple, true);
    }

    public static String generateSecurityCode(int length, RandomCodeLevel level) {
        return generateSecurityCode(length, level, true);
    }

    public static String generateSecurityCode(int length, RandomCodeLevel level, boolean repeat) {
        char[] _codes;
        if (level == RandomCodeLevel.Simple) {
            _codes = Arrays.copyOfRange(codes, 0, 9);
        } else if (level == RandomCodeLevel.Medium) {
            _codes = Arrays.copyOfRange(codes, 0, 33);
        } else {
            _codes = Arrays.copyOfRange(codes, 0, codes.length);
        }
        int n = _codes.length;
        if ((length > n) && (!repeat)) {
            throw new RuntimeException(String.format(
                    "调用RandomCodeUtils.generateSecurityCode(%1$s,%2$s,%3$s)出现异常，当repeat为%3$s时，传入参数%1$s不能大于%4$s",
                    length, level, repeat, n));
        }
        char[] result = new char[length];
        if (repeat) {
            for (int i = 0; i < result.length; i++) {
                int r = RandomUtils.nextInt(n);
                result[i] = _codes[r];
            }
        } else {
            for (int i = 0; i < result.length; i++) {
                int r = RandomUtils.nextInt(_codes.length);
                result[i] = _codes[r];
                _codes = removeItem(_codes, r);
            }
        }
        return String.valueOf(result);
    }

    public static char[] removeItem(char[] chars, int index) {
        if (index == 0) {
            return Arrays.copyOfRange(chars, 1, chars.length);
        }
        if (index == chars.length - 1) {
            return Arrays.copyOfRange(chars, 0, chars.length - 1);
        }
        char[] result = new char[chars.length - 1];
        char[] arr1 = Arrays.copyOfRange(chars, 0, index);
        System.arraycopy(arr1, 0, result, 0, arr1.length);
        char[] arr2 = Arrays.copyOfRange(chars, index + 1, chars.length);
        System.arraycopy(arr2, 0, result, arr1.length, arr2.length);
        return result;
    }

}

