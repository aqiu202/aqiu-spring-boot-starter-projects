package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

public class AndClassFilters extends AbstractFilters<Class<?>> {

    public AndClassFilters(ClassFilter... filters) {
        super(filters);
    }

// --Commented out by Inspection START (2024/10/16 10:39):
//    public AndClassFilters(Collection<Filter<Class<?>>> filters) {
//        super(filters);
//    }
// --Commented out by Inspection STOP (2024/10/16 10:39)

    @Override
    public boolean matches(Class<?> param) {
        Collection<ScanFilter<Class<?>>> filters = this.getFilters();
        if (filters.isEmpty()) {
            return true;
        }
        return filters.stream().allMatch((m) -> m.matches(param));
    }
}
