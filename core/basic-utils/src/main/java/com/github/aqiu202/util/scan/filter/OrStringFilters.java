package com.github.aqiu202.util.scan.filter;

import java.util.Collection;

public class OrStringFilters extends AbstractFilters<String> {

    public OrStringFilters(ClassNameFilter... matchers) {
        super(matchers);
    }

    public OrStringFilters(Collection<ScanFilter<String>> filters) {
        super(filters);
    }

    @Override
    public boolean matches(String param) {
        return this.getFilters().stream().anyMatch(m -> m.matches(param));
    }
}
