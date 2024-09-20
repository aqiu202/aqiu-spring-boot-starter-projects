package com.github.aqiu202.aop.keygen.impl;


import com.github.aqiu202.aop.keygen.KeyGenerator;
import java.lang.reflect.Method;
import java.util.StringJoiner;

public class MethodKeyGenerator implements KeyGenerator {

    @Override
    public String generate(Object target, Method method,
            Object... params) {
        StringJoiner joiner = new StringJoiner(",", "(", ")");
        for (Object param : params) {
            joiner.add(param == null ? "null" : param.getClass().getName());
        }
        return target.getClass().getName().concat(".").concat(method.getName())
                .concat(joiner.toString());
    }

}
