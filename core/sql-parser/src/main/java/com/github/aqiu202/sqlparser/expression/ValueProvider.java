package com.github.aqiu202.sqlparser.expression;

@FunctionalInterface
public interface ValueProvider<T> {

    T get();
}
