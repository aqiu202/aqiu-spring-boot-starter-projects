package com.github.aqiu202.util;

import java.security.SecureRandom;
import java.util.Random;

public abstract class RandomUtils {
    
    private static final Random INSTANCE = new SecureRandom();

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
