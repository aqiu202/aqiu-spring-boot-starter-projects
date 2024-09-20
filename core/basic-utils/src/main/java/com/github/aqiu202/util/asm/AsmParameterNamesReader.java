package com.github.aqiu202.util.asm;

import com.github.aqiu202.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Asm的方法参数名读取器
 */
public class AsmParameterNamesReader {

    private static final Map<String, List<String>> parameterNameMap = new HashMap<>();

    public List<String> readParameterNames(Method method) {
        String methodKey = this.buildMethodKey(method);
        return parameterNameMap.compute(methodKey, (k, v) -> {
            if (v == null) {
                List<String> parameterNames = new ParameterNameVisitor(method).visitMethodParameterNames();
                v = new ArrayList<>(parameterNames);
            }
            return v;
        });
    }

    protected String buildMethodKey(Method method) {
        String typeDescriptor = ReflectionUtils.getTypeDescriptor(method.getDeclaringClass());
        String methodDescriptor = ReflectionUtils.getMethodDescriptor(method);
        return String.format("%s#%s", typeDescriptor, methodDescriptor);
    }
}
