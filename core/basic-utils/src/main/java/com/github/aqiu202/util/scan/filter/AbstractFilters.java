package com.github.aqiu202.util.scan.filter;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

public abstract class AbstractFilters<T> extends LinkedHashSet<Filter<T>> implements Filters<T> {

    @SafeVarargs
    protected AbstractFilters(Filter<T>... filters) {
        this.addFilters(Arrays.asList(filters));
    }

    protected AbstractFilters(Collection<Filter<T>> filters) {
        this.addFilters(filters);
    }

}
