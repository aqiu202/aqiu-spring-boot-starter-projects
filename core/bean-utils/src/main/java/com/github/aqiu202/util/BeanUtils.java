package com.github.aqiu202.util;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.github.aqiu202.util.bean.JavaBeanMethod;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BeanUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    private static final Map<String, MethodAccess> methodCache = new ConcurrentHashMap<>();
    private static final Map<String, FieldAccess> fieldCache = new ConcurrentHashMap<>();
    private static final Map<String, List<JavaBeanMethod>> methodsCache = new ConcurrentHashMap<>();

    public static MethodAccess get(Class<?> clazz) {
        MethodAccess methodAccess;

        if (null != (methodAccess = methodCache.get(clazz.getName()))) {
            return methodAccess;
        }
        methodAccess = MethodAccess.get(clazz);
        methodCache.putIfAbsent(clazz.getName(), methodAccess);
        return methodAccess;
    }

    public static FieldAccess getFieldAccess(Class<?> clazz) {
        FieldAccess fieldAccess;

        if (null != (fieldAccess = fieldCache.get(clazz.getName()))) {
            return fieldAccess;
        }
        fieldAccess = FieldAccess.get(clazz);
        fieldCache.putIfAbsent(clazz.getName(), fieldAccess);
        return fieldAccess;
    }

    public static <F, T> void copyProperties(F from, T to) {
        MethodAccess fromMethodAccess = get(from.getClass());
        MethodAccess toMethodAccess = get(to.getClass());
        Class<?> fromClz = from.getClass();
        List<JavaBeanMethod> methods = getMethods(fromClz);
        for (JavaBeanMethod method : methods) {
            swap(fromMethodAccess, toMethodAccess, from, to, method);
        }
    }

    public static <F, T> void copyNotNullProperties(F from, T to) {
        MethodAccess fromMethodAccess = get(from.getClass());
        MethodAccess toMethodAccess = get(to.getClass());
        Class<?> fromClz = from.getClass();
        List<JavaBeanMethod> methods = getMethods(fromClz);
        for (JavaBeanMethod method : methods) {
            swapNotNull(fromMethodAccess, toMethodAccess, from, to, method);
        }
    }

    public static <F, T> void copyNotNullOrBlankProperties(F from, T to) {
        MethodAccess fromMethodAccess = get(from.getClass());
        MethodAccess toMethodAccess = get(to.getClass());
        Class<?> fromClz = from.getClass();
        List<JavaBeanMethod> methods = getMethods(fromClz);
        for (JavaBeanMethod method : methods) {
            swapNotNullOrBlank(fromMethodAccess, toMethodAccess, from, to, method);
        }
    }

    public static List<JavaBeanMethod> getMethods(Class<?> clz) {
        List<JavaBeanMethod> methods;
        if (null == (methods = methodsCache.get(clz.getName()))) {
            MethodAccess methodAccess = get(clz);
            methods = new ArrayList<>();
            fields(clz, methods);
            for (JavaBeanMethod method : methods) {
                try {
                    method.setWriteIndex(methodAccess
                            .getIndex(method.getWriteMethodName(), method.getType()));
                } catch (Exception e) {
                    method.setWriteIndex(-1);
                }
                try {
                    method.setReadIndex(methodAccess.getIndex(method.getReadMethodName(), 0));
                } catch (Exception e) {
                    method.setReadIndex(-1);
                }
            }
            methodsCache.putIfAbsent(clz.getName(), methods);
        }
        return methods;
    }

    private static void fields(Class<?> clz, List<JavaBeanMethod> methods) {
        Class<?> parentClz;
        if ((parentClz = clz.getSuperclass()) != null && !parentClz.equals(Object.class)) {
            fields(parentClz, methods);
        }

        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            if (isJavaBeanField(field)) {
                methods.add(JavaBeanMethod.of(field));
            }
        }
    }

    private static boolean isJavaBeanField(Field field) {
        int m = field.getModifiers();
        return !(Modifier.isFinal(m) || Modifier.isStatic(m));
    }

    private static <F, T> void swap(MethodAccess fma, MethodAccess tma,
                                    F f, T t, JavaBeanMethod method) {
        Object value = fma.invoke(f, method.getReadIndex());
        invoke(tma, t, method.getWriteMethodName(), value);
    }

    private static <F, T> void swapNotNull(MethodAccess fma, MethodAccess tma,
                                           F f, T t, JavaBeanMethod method) {
        Object value = fma.invoke(f, method.getReadIndex());
        if (null != value) {
            invoke(tma, t, method.getWriteMethodName(), value);
        }
    }

    private static <F, T> void swapNotNullOrBlank(MethodAccess fma, MethodAccess tma,
                                                  F f, T t, JavaBeanMethod method) {
        Object value = fma.invoke(f, method.getReadIndex());
        if (null != value) {
            if (method.getType().equals(String.class) && !StringUtils.hasText(value.toString())) {
                return;
            }
            invoke(tma, t, method.getWriteMethodName(), value);
        }
    }

    private static void invoke(MethodAccess ma, Object obj, String method, Object... args) {
        try {
            ma.invoke(obj, method, args);
        } catch (RuntimeException e) {
            log.error("", e);
        }
    }

}