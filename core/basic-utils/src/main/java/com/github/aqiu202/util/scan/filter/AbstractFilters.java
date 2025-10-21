package com.github.aqiu202.util.scan.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public abstract class AbstractFilters<T> extends LinkedHashSet<ScanFilter<T>> implements ScanFilters<T> {

    @SafeVarargs
    protected AbstractFilters(ScanFilter<T>... filters) {
        this.addFilters(Arrays.asList(filters));
    }

    protected AbstractFilters(Collection<ScanFilter<T>> filters) {
        this.addFilters(filters);
    }

}
