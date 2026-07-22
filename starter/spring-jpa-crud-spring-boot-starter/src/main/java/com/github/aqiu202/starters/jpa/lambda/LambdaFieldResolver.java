package com.github.aqiu202.starters.jpa.lambda;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LambdaFieldResolver {

    public static String resolveFieldName(LambdaField<?, ?> field) {
        Method method;
        try {
            method = field.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(true);
            SerializedLambda lambda = (SerializedLambda) method.invoke(field);
            String methodName = lambda.getImplMethodName();
            return methodToProperty(methodName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else if (name.startsWith("get") || name.startsWith("set")) {
            name = name.substring(3);
        }
        // 首字母小写处理
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }
}
