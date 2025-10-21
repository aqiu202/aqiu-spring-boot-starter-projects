package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

public class OrClassFilters extends AbstractFilters<Class<?>> {

    public OrClassFilters(ClassFilter... matchers) {
        super(matchers);
    }

    public OrClassFilters(Collection<ScanFilter<Class<?>>> filters) {
        super(filters);
    }

    @Override
    public boolean matches(Class<?> param) {
        return this.getFilters().stream().anyMatch(m -> m.matches(param));
    }
}
