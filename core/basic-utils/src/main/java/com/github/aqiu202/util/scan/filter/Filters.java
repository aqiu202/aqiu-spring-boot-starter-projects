package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

/**
 * 多重的过滤器
 * @param <T> 决策参数类型
 */
public interface Filters<T> extends Filter<T>, Collection<Filter<T>> {

    /**
     * 添加过滤器
     * @param filter 过滤器
     */
    default void addFilter(Filter<T> filter) {
        this.add(filter);
    }

    /**
     * 批量添加过滤器
     * @param filters 过滤器集合
     */
    default void addFilters(Collection<Filter<T>> filters) {
        this.addAll(filters);
    }

    default Collection<Filter<T>> getFilters() {
        return this;
    }
}
