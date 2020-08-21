package com.github.aqiu202.captcha.util;

import java.awt.Color;

public final class ColorUtils {

    private ColorUtils() {
    }

    public static final int COLOR_RGB_START = 0;
    public static final int COLOR_RGB_DEEP_END = 85;
    public static final int COLOR_RGB_MIDDLE_START = 86;
    public static final int COLOR_RGB_MIDDLE_END = 170;
    public static final int COLOR_RGB_LIGHT_START = 171;
    public static final int COLOR_RGB_END = 255;

    public static Color randomColor(int start, int end) {
        return new Color(randomRgbItem(start, end), randomRgbItem(start, end),
                randomRgbItem(start, end));
    }

    public static Color randomColor() {
        return randomColor(COLOR_RGB_START, COLOR_RGB_END);
    }

    public static Color randomDeepColor() {
        return randomColor(COLOR_RGB_START, COLOR_RGB_DEEP_END);
    }

    public static Color randomLightColor() {
        return randomColor(COLOR_RGB_LIGHT_START, COLOR_RGB_END);
    }

    public static Color randomMiddleColor() {
        return randomColor(COLOR_RGB_MIDDLE_START, COLOR_RGB_MIDDLE_END);
    }

    private static int randomRgbItem(int start, int end) {
        start = validRgb(start);
        end = validRgb(end);
        int randomScope = Math.abs(end - start) + 1;
        return Math.min(start, end) + RandomUtils.nextInt(randomScope);
    }

    private static int validRgb(int value) {
        if (value > 255) {
            value = 255;
        } else if (value < 0) {
            value = 0;
        }
        return value;
    }

    public static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    public static int getRandomIntColor(int start, int end) {
        int[] rgb = getRandomRgb(start, end);
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = RandomUtils.nextInt(256);
        }
        return rgb;
    }

    private static int[] getRandomRgb(int start, int end) {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = randomRgbItem(start, end);
        }
        return rgb;
    }

}
