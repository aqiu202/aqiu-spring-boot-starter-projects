package com.github.aqiu202.aop.spel;

import java.lang.reflect.Method;
import org.springframework.expression.EvaluationContext;

@FunctionalInterface
public interface EvaluationFiller {

    void fill(EvaluationContext context, Object target, Method method, Object[] parameters);
}
