package com.github.aqiu202.util;

import com.github.aqiu202.util.type.Primitives;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Supplier;

public abstract class ClassUtils {

    private static final List<Class<?>> basicNumberTypes = Arrays.asList(Integer.TYPE, Short.TYPE, Byte.TYPE,
            Long.TYPE, Float.TYPE, Double.TYPE);

    private static final List<Class<?>> basicWrapTypes = Arrays.asList(Integer.class, Short.class, Byte.class,
            Boolean.class, Long.class, Float.class,
            Double.class, Character.class);

    private static final ClassLoader classLoader;

    static {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            try {
                cl = ClassLoader.getSystemClassLoader();
            } catch (Throwable ignored) {
            }
        }
        classLoader = cl;
    }

    public static Class<?> resolveClass(String className) {
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Optional<Class<?>> handleClass(String className) {
        Class<?> result;
        try {
            result = classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            result = null;
        }
        return Optional.ofNullable(result);
    }

    public static <T> T newInstance(@Nonnull Class<T> type) {
        try {
            Constructor<T> constructor = type.getConstructor();
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T newInstance(@Nonnull Class<T> type, Class<?>[] paramTypes, Object[] params) {
        try {
            Constructor<T> constructor = type.getConstructor(paramTypes);
            ReflectionUtils.makeAccessible(constructor);
            return constructor.newInstance(params);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isCollection(@Nonnull Class<?> type) {
        return isAssignableFrom(Collection.class, type);
    }

    public static boolean isMap(@Nonnull Class<?> type) {
        return isAssignableFrom(Map.class, type);
    }

    public static boolean isDate(@Nonnull Class<?> type) {
        return isAssignableFrom(Date.class, type) || isAssignableFrom(TemporalAccessor.class, type);
    }

    public static boolean isNumber(@Nonnull Class<?> type) {
        return (type.isPrimitive() && basicNumberTypes.contains(type)) || isAssignableFrom(Number.class, type);
    }

    public static boolean isCollection(@Nullable Object obj) {
        return obj instanceof Collection;
    }

    public static boolean isMap(@Nullable Object obj) {
        return obj instanceof Map;
    }

    public static boolean isDate(@Nullable Object obj) {
        return obj instanceof Date || obj instanceof TemporalAccessor;
    }

    public static boolean isNumber(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        Class<?> targetType = obj.getClass();
        return (targetType.isPrimitive() && basicNumberTypes.contains(targetType)) || obj instanceof Number;
    }

    public static boolean isBasicWrapType(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        return basicWrapTypes.contains(obj.getClass());
    }

    public static boolean isBasicWrapType(Class<?> type) {
        return basicWrapTypes.contains(type);
    }

    public static boolean isBasicType(Class<?> type) {
        return type.isPrimitive() || basicWrapTypes.contains(type) || String.class.equals(type);
    }

    private static Class<?> normalize(Class<?> cl) {
        return cl.isPrimitive() ? Primitives.wrap(cl) : cl;
    }

    public static boolean notAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return !isAssignableFrom(cl1, cl2);
    }

    public static boolean isAssignableFrom(Class<?> cl1, Class<?> cl2) {
        return normalize(cl1).isAssignableFrom(normalize(cl2));
    }

    public static boolean isCustomClass(@Nullable Class<?> clz) {
        return clz != null && clz.getClassLoader() != null;
    }

    public static String beanName(Class<?> clz) {
        String simpleName = clz.getSimpleName();
        return Character.toLowerCase(simpleName.charAt(0)) + simpleName.substring(1);
    }

    /**
     * 获取类型的所有父类及接口集合（不包括Object.class）
     *
     * @param child 类型
     * @return 类型的所有父类及接口集合
     */
    public static Set<Class<?>> resolveSupperClass(Class<?> child) {
        Set<Class<?>> classes = new LinkedHashSet<>();
        if (!isRootClass(child)) {
            classes.add(child);
        }
        if (hasParentOrInterface(child)) {
            Class<?>[] interfaces = child.getInterfaces();
            for (Class<?> i : interfaces) {
                classes.add(i);
                if (hasParentOrInterface(i)) {
                    Set<Class<?>> is = resolveSupperClass(i);
                    if (!is.isEmpty()) {
                        classes.addAll(is);
                    }
                }
            }
            Class<?> superclass = child.getSuperclass();
            if (!isRootClass(superclass)) {
                classes.add(superclass);
                Set<Class<?>> cs = resolveSupperClass(superclass);
                if (!cs.isEmpty()) {
                    classes.addAll(cs);
                }
            }
        }
        return classes;
    }

    /**
     * 是否有父类或接口
     *
     * @param c 类型
     * @return 是否有父类或接口
     */
    private static boolean hasParentOrInterface(Class<?> c) {
        // 如果是接口，判断是否有父接口即可
        if (c.isInterface()) {
            return c.getInterfaces().length != 0;
        }
        // 如果是类，判断是否有父类或接口
        Class<?> superclass = c.getSuperclass();
        return !isRootClass(superclass) || c.getInterfaces().length != 0;
    }

    /**
     * 是否是根类
     *
     * @param c 类型
     * @return 是否是根类
     */
    public static boolean isRootClass(Class<?> c) {
        return c == null || Object.class.equals(c) || c.getClassLoader() == null;
    }

    public static boolean isActualType(Type type) {
        return type instanceof ParameterizedType;
    }

    public static Type getActualType(ParameterizedType type, int index) {
        if (index < 0) {
            index = 0;
        }
        Type[] types = type.getActualTypeArguments();
        if (types.length > index) {
            return types[index];
        }
        return null;
    }

    public static Type findValueType(ParameterizedType parameterizedType) {
        Class<?> rawType = (Class<?>) parameterizedType.getRawType();
        if (isCollection(rawType)
                || Supplier.class.isAssignableFrom(rawType)
                || Optional.class.isAssignableFrom(rawType)) {
            return getActualType(parameterizedType, 0);
        } else if (isMap(rawType)) {
            return getActualType(parameterizedType, 1);
        }
        return rawType;
    }

}
