package com.github.aqiu202.util.scan.filter;

/**
 * 扫描的过滤器
 * @param <T> 过滤类型
 */
public interface ScanFilter<T> {

    boolean matches(T param);
}
