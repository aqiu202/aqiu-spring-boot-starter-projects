package com.github.aqiu202.starters.jpa.lambda;

import java.io.Serializable;
import java.util.function.Function;

@FunctionalInterface
public interface LambdaField<E,T> extends Function<E, T>, Serializable {

}
