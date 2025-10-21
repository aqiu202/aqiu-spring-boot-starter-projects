package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

/**
 * 多重的过滤器
 * @param <T> 决策参数类型
 */
public interface ScanFilters<T> extends ScanFilter<T>, Collection<ScanFilter<T>> {

    /**
     * 添加过滤器
     * @param filter 过滤器
     */
    default void addFilter(ScanFilter<T> filter) {
        this.add(filter);
    }

    /**
     * 批量添加过滤器
     * @param filters 过滤器集合
     */
    default void addFilters(Collection<ScanFilter<T>> filters) {
        this.addAll(filters);
    }

    default Collection<ScanFilter<T>> getFilters() {
        return this;
    }
}
