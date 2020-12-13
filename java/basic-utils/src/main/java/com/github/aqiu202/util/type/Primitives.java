package com.github.aqiu202.util.type;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * <pre>{@link Primitives}</pre>
 *
 * @author aqiu 2020/12/13 0:11
 **/
public final class Primitives {
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    private Primitives() {
    }

    private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public static Set<Class<?>> allPrimitiveTypes() {
        return PRIMITIVE_TO_WRAPPER_TYPE.keySet();
    }

    public static Set<Class<?>> allWrapperTypes() {
        return WRAPPER_TO_PRIMITIVE_TYPE.keySet();
    }

    public static boolean isWrapperType(Class<?> type) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(Objects.requireNonNull(type));
    }

    public static <T> Class<T> wrap(Class<T> type) {
        assert type != null;
        Class<T> wrapped = (Class)PRIMITIVE_TO_WRAPPER_TYPE.get(type);
        return wrapped == null ? type : wrapped;
    }

    public static <T> Class<T> unwrap(Class<T> type) {
        assert type != null;
        Class<T> unwrapped = (Class)WRAPPER_TO_PRIMITIVE_TYPE.get(type);
        return unwrapped == null ? type : unwrapped;
    }

    static {
        Map<Class<?>, Class<?>> primToWrap = new LinkedHashMap(16);
        Map<Class<?>, Class<?>> wrapToPrim = new LinkedHashMap(16);
        add(primToWrap, wrapToPrim, Boolean.TYPE, Boolean.class);
        add(primToWrap, wrapToPrim, Byte.TYPE, Byte.class);
        add(primToWrap, wrapToPrim, Character.TYPE, Character.class);
        add(primToWrap, wrapToPrim, Double.TYPE, Double.class);
        add(primToWrap, wrapToPrim, Float.TYPE, Float.class);
        add(primToWrap, wrapToPrim, Integer.TYPE, Integer.class);
        add(primToWrap, wrapToPrim, Long.TYPE, Long.class);
        add(primToWrap, wrapToPrim, Short.TYPE, Short.class);
        add(primToWrap, wrapToPrim, Void.TYPE, Void.class);
        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }
}
