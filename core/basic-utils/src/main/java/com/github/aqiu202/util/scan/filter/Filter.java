package com.github.aqiu202.util.scan.filter;

/**
 * 扫描的过滤器
 * @param <T> 过滤类型
 */
public interface Filter<T> {

    boolean matches(T param);
}
