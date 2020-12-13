package com.github.aqiu202.aop.keygen.impl;


import com.github.aqiu202.aop.keygen.KeyGenerator;
import java.lang.reflect.Method;

public class SimpleKeyGenerator implements KeyGenerator {

    @Override
    public String generate(Object target, Method method,
            Object... params) {
        return target.getClass().getName() + "." + method.getName() + ":" + params.length;
    }

}
