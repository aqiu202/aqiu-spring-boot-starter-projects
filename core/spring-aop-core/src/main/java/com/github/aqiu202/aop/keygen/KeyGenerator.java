package com.github.aqiu202.aop.keygen;

import java.lang.reflect.Method;

@FunctionalInterface
public interface KeyGenerator {

    String DEFAULT_METHOD_KEY_GENERATOR = "methodKeyGenerator";

    /**
     * Generate a key for the given method and its parameters.
     * @param target the target instance
     * @param method the method being called
     * @param params the method parameters (with any var-args expanded)
     * @return a generated key
     */
    String generate(Object target, Method method, Object... params);

}
