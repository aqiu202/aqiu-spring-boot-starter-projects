package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

/**
 * 多重className过滤器（需要所有过滤器同时满足）
 */
public class AndNameFilters extends AbstractFilters<String> {

    public AndNameFilters(ClassNameFilter... matchers) {
        super(matchers);
    }

    public AndNameFilters(Collection<Filter<String>> filters) {
        super(filters);
    }

    @Override
    public boolean matches(String param) {
        Collection<Filter<String>> filters = this.getFilters();
        if (filters.isEmpty()) {
            return true;
        }
        return filters.stream().allMatch((m) -> m.matches(param));
    }
}
