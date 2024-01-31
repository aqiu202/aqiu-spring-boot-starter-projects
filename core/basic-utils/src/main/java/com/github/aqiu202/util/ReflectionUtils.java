package com.github.aqiu202.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class ReflectionUtils {

    public enum ScanDirection {
        /**
         * 扫描方向：从子类向父类扫描
         */
        DIRECTION_CHILD_FIRST,
        /**
         * 扫描方向：从父类向子类扫描
         */
        DIRECTION_PARENT_FIRST
    }

    /**
     * 方法描述的缓存，提高反射性能
     */
    private static final ConcurrentMap<String, ClassMethods> methodsCache = new ConcurrentHashMap<>();
    /**
     * 字段描述的缓存，提高反射性能
     */
    private static final ConcurrentMap<String, ClassFields> fieldsCache = new ConcurrentHashMap<>();

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

    /**
     * 使构造器可访问
     *
     * @param ctor 构造器
     */
    public static void makeAccessible(@Nonnull Constructor<?> ctor) {
        if ((!Modifier.isPublic(ctor.getModifiers()) ||
                !Modifier.isPublic(ctor.getDeclaringClass().getModifiers())) && !ctor.isAccessible()) {
            ctor.setAccessible(true);
        }
    }

    /**
     * 使方法可访问
     *
     * @param method 方法
     */
    public static void makeAccessible(@Nonnull Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) ||
                !Modifier.isPublic(method.getDeclaringClass().getModifiers())) && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 使字段可访问
     *
     * @param field 字段
     */
    public static void makeAccessible(@Nonnull Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    public static Object newInstance(Constructor<?> constructor, Object... args) {
        try {
            makeAccessible(constructor);
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("反射创建实例异常", e);
        }
    }

    /**
     * 获取所有的方法（包含私有方法，但不包含abstract和static修饰的方法）
     *
     * @param type 类型
     * @return 所有方法集合
     */
    public static ClassMethods getAllMethod(@Nonnull Class<?> type) {
        return getAllMethod(type, ScanDirection.DIRECTION_CHILD_FIRST);
    }

    /**
     * 获取所有的方法（包含私有方法，但不包含abstract和static修饰的方法）
     *
     * @param type 类型
     * @param direction 扫描方向
     * @return 所有方法集合
     */
    public static ClassMethods getAllMethod(@Nonnull Class<?> type, ScanDirection direction) {
        String className = type.getName();
        ClassMethods result;
        if ((result = methodsCache.get(className)) != null) {
            return result;
        }
        Class<?> clazz = type;
        List<Class<?>> types = new ArrayList<>();
        while (clazz != null && !clazz.equals(Object.class)) {
            types.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (ScanDirection.DIRECTION_PARENT_FIRST == direction) {
            Collections.reverse(types);
        }
        List<Method> methods = types.stream()
                .flatMap(ReflectionUtils::resolveMethods)
                .collect(Collectors.toList());
        result = new ClassMethods(methods);
        methodsCache.put(className, result);
        return result;
    }

    /**
     * 获取类所有的非抽象和静态方法
     * @param type 类
     * @return 方法集合
     */
    private static Stream<Method> resolveMethods(Class<?> type) {
        return Arrays.stream(type.getDeclaredMethods())
                .filter(m -> !(Modifier.isAbstract(m.getModifiers()) || Modifier.isStatic(m.getModifiers())));
    }

    /**
     * 获取所有的方法（不包含私有方法）
     *
     * @param type 类型
     * @return 所有方法集合
     */
    public static ClassMethods getMethods(@Nonnull Class<?> type) {
        return new ClassMethods(getAllMethod(type).stream()
                .filter(m -> !Modifier.isPrivate(m.getModifiers()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public static Method getMethod(@Nonnull Class<?> type, String methodName, Class<?>... args) {
        return getAllMethod(type).getMethod(methodName, args);
    }

    public static Object invokeMethod(@Nonnull Method method, Object instance, Object... args) {
        try {
            makeAccessible(method);
            return method.invoke(instance, args);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射调用方法异常", e);
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException();
            throw new RuntimeException(t.getMessage(), t);
        }
    }

    /**
     * 获取所有的属性（包含私有属性，但不包含final和static修饰的属性）
     *
     * @param type 类型
     * @return 所有属性集合
     */
    public static ClassFields getAllField(@Nonnull Class<?> type) {
        return getAllField(type, ScanDirection.DIRECTION_CHILD_FIRST);
    }

    public static ClassFields getAllField(@Nonnull Class<?> type, ScanDirection direction) {
        String className = type.getName();
        ClassFields result;
        if ((result = fieldsCache.get(className)) != null) {
            return result;
        }
        List<Class<?>> types = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != null && !clazz.equals(Object.class)) {
            types.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (ScanDirection.DIRECTION_PARENT_FIRST == direction) {
            Collections.reverse(types);
        }
        List<Field> fields = types.stream()
                .flatMap(ReflectionUtils::resolveFields)
                .collect(Collectors.toList());
        result = new ClassFields(fields);
        fieldsCache.put(className, result);
        return result;
    }

    private static Stream<Field> resolveFields(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields())
                .filter(f -> !(Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())));
    }

    /**
     * 获取所有的属性（不包含私有属性）
     *
     * @param type 类型
     * @return 所有属性集合
     */
    public static ClassFields getFields(@Nonnull Class<?> type) {
        return new ClassFields(getAllField(type).stream()
                .filter(f -> !Modifier.isPrivate(f.getModifiers()))
                .collect(Collectors.toCollection(LinkedHashSet::new)));
    }

    public static Field getField(@Nonnull Class<?> type, String fieldName) {
        return getAllField(type).getField(fieldName);
    }

    public static Constructor<?>[] getDeclaredConstructors(@Nonnull Class<?> type) {
        return type.getDeclaredConstructors();
    }

    public static Constructor<?>[] getConstructors(@Nonnull Class<?> type) {
        return type.getConstructors();
    }

    public static Constructor<?> getDefaultConstructor(@Nonnull Class<?> type) {
        Constructor<?>[] constructors = getDeclaredConstructors(type);
        if (constructors.length == 0) {
            throw new RuntimeException("没有找到构造函数");
        }
        if (constructors.length == 1) {
            return constructors[0];
        }
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }
        return null;
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target 对象
     * @param field  字段
     * @return 字段值
     */
    public static Object getValue(@Nullable Object target, Field field) {
        if (field == null) {
            return null;
        }
        try {
            makeAccessible(field);
            return field.get(target);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射获取字段值异常", e);
        }
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target           对象
     * @param complexFieldName 字段名称 以点"."拼接
     * @return 字段值
     */
    public static Object getComplexValue(@Nonnull Object target, String complexFieldName) {
        return getComplexValue(target, complexFieldName, "\\.");
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target           对象
     * @param complexFieldName 字段名称
     * @param separator        字段分隔符
     * @return 字段值
     */
    public static Object getComplexValue(@Nonnull Object target, String complexFieldName, String separator) {
        if (StringUtils.isBlank(complexFieldName)) {
            return null;
        }
        return getValue(target, complexFieldName.split(separator));
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target     对象
     * @param fieldNames 字段名称数组
     * @return 字段值
     */
    public static Object getValue(@Nonnull Object target, String[] fieldNames) {
        for (String fieldName : fieldNames) {
            target = getValue(target, fieldName);
            if (target == null) {
                return null;
            }
        }
        return target;
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target    对象
     * @param fieldName 字段名称
     * @return 字段值
     */
    public static Object getValue(@Nonnull Object target, String fieldName) {
        Class<?> clz = target.getClass();
        Field field = getField(clz, fieldName);
        return getValue(target, field);
    }

    /**
     * 给对象的某个字段赋值
     *
     * @param target    对象
     * @param fieldName 字段名称
     * @param value     字段值
     * @return 字段值
     */
    public static Object setValue(@Nonnull Object target, String fieldName, Object value) {
        Class<?> clz = target.getClass();
        Field field = getField(clz, fieldName);
        return setValue(target, field, value);
    }

    /**
     * 获取对象某个字段的值
     *
     * @param target 对象
     * @param field  字段
     * @param value  字段值
     * @return 字段值
     */
    public static Object setValue(@Nonnull Object target, Field field, Object value) {
        try {
            makeAccessible(field);
            field.set(target, value);
            return value;
        } catch (IllegalAccessException e) {
            throw new RuntimeException("反射设置字段值异常", e);
        }
    }

    public static <T extends Annotation> T getAnnotation(@Nonnull AnnotatedElement element,
                                                         Class<T> annotationType) {
        T[] annotations = element.getDeclaredAnnotationsByType(annotationType);
        return annotations.length > 0 ? annotations[0] : null;
    }

    public static <T extends Annotation> List<T> getAnnotations(@Nonnull AnnotatedElement element,
                                                                Class<T> annotationType) {
        return Arrays.asList(element.getDeclaredAnnotationsByType(annotationType));
    }

    public static <T extends Annotation> T findAnnotation(@Nonnull AnnotatedElement element,
                                                          Class<T> annotationType) {
        T[] annotations = element.getAnnotationsByType(annotationType);
        return annotations.length > 0 ? annotations[0] : null;
    }

    public static <T extends Annotation> List<T> findAnnotations(@Nonnull AnnotatedElement element,
                                                                 Class<T> annotationType) {
        return Arrays.asList(element.getAnnotationsByType(annotationType));
    }

    public static boolean isAnnotationPresent(@Nonnull AnnotatedElement element, Class<? extends Annotation> annotation) {
        return element.isAnnotationPresent(annotation);
    }

    public static boolean hasAnnotationPresent(@Nonnull AnnotatedElement element, Class<? extends Annotation>... annotationTypes) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (isAnnotationPresent(element, annotationType)) {
                return true;
            }
        }
        return false;
    }


    public static boolean hasAnnotation(@Nonnull AnnotatedElement element, Class<? extends Annotation>... annotationTypes) {
        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if (hasAnnotation(element, annotationType)) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasAnnotation(@Nonnull AnnotatedElement element, Class<? extends Annotation> annotationType) {
        return element.getAnnotationsByType(annotationType).length > 0;
    }

    public static String generateMethodKey(@Nonnull Method method) {
        return generateKey(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }

    public static String generateKey(@Nonnull Class<?> type, String methodName, Class<?>... parameterTypes) {
        return type.getName() + "." + generateKey(methodName, parameterTypes);
    }

    public static String generateKey(String methodName, Class<?>... parameterTypes) {
        String parameterNames = Arrays.stream(parameterTypes).map(Class::getName).collect(Collectors.joining(","));
        return methodName + "(" + parameterNames + ")";
    }

    public static class ClassFields {
        private final Map<String, Field> fieldMap = new HashMap<>();
        private final List<Field> fields;

        ClassFields(Collection<Field> fields) {
            this.fields = new ArrayList<>(fields);
            for (Field field : fields) {
                fieldMap.putIfAbsent(field.getName(), field);
            }
        }

        public Field getField(String name) {
            return this.fieldMap.get(name);
        }

        public List<Field> getFields() {
            return this.fields;
        }

        public Stream<Field> stream() {
            return this.getFields().stream();
        }

    }

    public static class ClassMethods {

        private final Map<String, Method> methodMap = new HashMap<>();
        private final List<Method> methods;

        ClassMethods(Collection<Method> methods) {
            this.methods = new ArrayList<>(methods);
            for (Method method : methods) {
                methodMap.putIfAbsent(generateKey(method.getName(), method.getParameterTypes()), method);
            }
        }

        public Method getMethod(String methodName, Class<?>... args) {
            return methodMap.get(generateKey(methodName, args));
        }

        public List<Method> getMethods() {
            return this.methods;
        }

        public Stream<Method> stream() {
            return this.getMethods().stream();
        }
    }

}
