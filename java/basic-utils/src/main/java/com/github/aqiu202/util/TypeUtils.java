package com.github.aqiu202.util;

import com.github.aqiu202.util.type.Primitives;

/**
 * <pre>{@link TypeUtils}</pre>
 *
 * @author aqiu 2020/12/13 0:14
 **/
public abstract class TypeUtils {

    private static Class<?> normalize(Class<?> cl) {
        return cl.isPrimitive() ? Primitives.wrap(cl) : cl;
    }

    public static boolean notAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return !isAssignableFrom(cl1, cl2);
    }

    public static boolean isAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return normalize(cl1).isAssignableFrom(normalize(cl2));
    }

}
