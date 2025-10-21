package com.github.aqiu202.util.scan.filter;

import java.util.Collection;
/**
 * 多重class过滤器（需要所有过滤器同时满足）
 */
public class AndClassFilters extends AbstractFilters<Class<?>> {

    public AndClassFilters(ClassFilter... filters) {
        super(filters);
    }

    public AndClassFilters(Collection<ScanFilter<Class<?>>> filters) {
        super(filters);
    }

    @Override
    public boolean matches(Class<?> param) {
        Collection<ScanFilter<Class<?>>> filters = this.getFilters();
        if (filters.isEmpty()) {
            return true;
        }
        return filters.stream().allMatch((m) -> m.matches(param));
    }
}
