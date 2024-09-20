package com.github.aqiu202.util;

import com.esotericsoftware.reflectasm.FieldAccess;
import com.esotericsoftware.reflectasm.MethodAccess;
import com.github.aqiu202.util.bean.JavaBeanMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class BeanUtils {

    private static final Logger log = LoggerFactory.getLogger(BeanUtils.class);

    private static final Map<String, MethodAccess> methodCache = new ConcurrentHashMap<>();
    private static final Map<String, FieldAccess> fieldCache = new ConcurrentHashMap<>();
    private static final Map<String, List<JavaBeanMethod>> methodsCache = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> methodNamesCache = new ConcurrentHashMap<>();

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

    public static Set<String> getMethodNames(Class<?> clazz) {
        return methodNamesCache.computeIfAbsent(clazz.getName(), k -> {
            MethodAccess methodAccess = get(clazz);
            return new HashSet<>(Arrays.asList(methodAccess.getMethodNames()));
        });
    }

    public static void copyProperties(Object from, Object to) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        List<JavaBeanMethod> methods = getIntersectionMethods(fromClass, toClass);
        MethodAccess fromMethodAccess = get(fromClass);
        MethodAccess toMethodAccess = get(toClass);
        for (JavaBeanMethod method : methods) {
            swap(fromMethodAccess, toMethodAccess, from, to, method);
        }
    }

    private static List<JavaBeanMethod> getIntersectionMethods(Class<?> fromClass, Class<?> toClass) {
        Set<String> readMethodNames = getMethodNames(fromClass);
        Set<String> writeMethodNames = getMethodNames(toClass);
        return getMethods(fromClass)
                .stream()
                .filter(method -> readMethodNames.contains(method.getReadMethodName())
                        && writeMethodNames.contains(method.getWriteMethodName()))
                .collect(Collectors.toList());
    }

    public static void copyNotNullProperties(Object from, Object to) {
        Class<?> fromClass = from.getClass();
        Class<?> toClass = to.getClass();
        List<JavaBeanMethod> methods = getIntersectionMethods(fromClass, toClass);
        MethodAccess fromMethodAccess = get(fromClass);
        MethodAccess toMethodAccess = get(toClass);
        for (JavaBeanMethod method : methods) {
            swapNotNull(fromMethodAccess, toMethodAccess, from, to, method);
        }
    }

    public static void copyNotNullOrBlankProperties(Object from, Object to) {
        Class<?> fromClass = from.getClass();
        MethodAccess fromMethodAccess = get(fromClass);
        Class<?> toClass = to.getClass();
        MethodAccess toMethodAccess = get(toClass);
        List<JavaBeanMethod> methods = getIntersectionMethods(fromClass, toClass);
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
                    log.warn("setter方法：{}未找到:{}", method.getWriteMethodName(), e.getMessage());
                }
                try {
                    method.setReadIndex(methodAccess.getIndex(method.getReadMethodName(), 0));
                } catch (Exception e) {
                    log.warn("getter方法：{}未找到:{}", method.getReadMethodName(), e.getMessage());
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

    private static void swap(MethodAccess fma, MethodAccess tma,
                             Object f, Object t, JavaBeanMethod method) {
        Object value = fma.invoke(f, method.getReadIndex());
        invoke(tma, t, method.getWriteMethodName(), value);
    }

    private static void swapNotNull(MethodAccess fma, MethodAccess tma,
                                    Object f, Object t, JavaBeanMethod method) {
        Object value = fma.invoke(f, method.getReadIndex());
        if (null != value) {
            invoke(tma, t, method.getWriteMethodName(), value);
        }
    }

    private static void swapNotNullOrBlank(MethodAccess fma, MethodAccess tma,
                                           Object f, Object t, JavaBeanMethod method) {
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
            log.error("反射调用方法异常", e);
        }
    }

}