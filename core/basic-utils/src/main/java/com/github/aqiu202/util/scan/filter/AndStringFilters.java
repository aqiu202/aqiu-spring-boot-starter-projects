package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

public class AndStringFilters extends AbstractFilters<String> {

    public AndStringFilters(ClassNameFilter... matchers) {
        super(matchers);
    }

    public AndStringFilters(Collection<ScanFilter<String>> filters) {
        super(filters);
    }


    @Override
    public boolean matches(String param) {
        Collection<ScanFilter<String>> filters = this.getFilters();
        if (filters.isEmpty()) {
            return true;
        }
        return filters.stream().allMatch((m) -> m.matches(param));
    }
}
