package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

public interface ScanFilters<T> extends ScanFilter<T>, Collection<ScanFilter<T>> {

    default void addFilter(ScanFilter<T> filter) {
        this.add(filter);
    }

    default void addFilters(Collection<ScanFilter<T>> filters) {
        this.addAll(filters);
    }

    default Collection<ScanFilter<T>> getFilters() {
        return this;
    }
}
