package com.github.aqiu202.excel.model;

@FunctionalInterface
public interface ReadDataFilter<T> {

    boolean test(T data);
}
