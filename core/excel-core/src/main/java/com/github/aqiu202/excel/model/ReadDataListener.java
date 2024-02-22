package com.github.aqiu202.excel.model;

@FunctionalInterface
public interface ReadDataListener<T> {

    void onData(T data);
}
