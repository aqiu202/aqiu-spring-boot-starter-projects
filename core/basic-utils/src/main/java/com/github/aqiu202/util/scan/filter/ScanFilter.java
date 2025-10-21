package com.github.aqiu202.util.scan.filter;

public interface ScanFilter<T> {

    boolean matches(T param);
}
