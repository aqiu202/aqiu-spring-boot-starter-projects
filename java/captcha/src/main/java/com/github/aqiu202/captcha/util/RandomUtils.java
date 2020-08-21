package com.github.aqiu202.captcha.util;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomUtils {

    private RandomUtils() {
    }

    private final static Random INSTANCE = new SecureRandom();

    public static Random single() {
        return INSTANCE;
    }

    public static float nextFloat() {
        return INSTANCE.nextFloat();
    }

    public static int nextInt() {
        return INSTANCE.nextInt();
    }

    public static int nextInt(int bound) {
        return INSTANCE.nextInt(bound);
    }

    public static boolean nextBoolean() {
        return INSTANCE.nextBoolean();
    }

}
