package com.github.aqiu202.aop.pointcut;

import com.github.aqiu202.util.SpELUtils;
import com.github.aqiu202.util.spel.EvaluationFiller;

import java.lang.reflect.Method;

public interface SPelKeyHandler {
    default String processKey(String key, Object target, Method method, Object[] parameters, EvaluationFiller evaluationFiller) {
        if (SpELUtils.isSpEL(key)) {
            key = SpELUtils.handleSpEl(key, target, method, parameters, evaluationFiller);
        } else {
            key = SpELUtils.handleNormalKey(key);
        }
        return key;
    }
}
