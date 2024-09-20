package com.github.aqiu202.util.scan.filter;

import java.util.Collection;
/**
 * 多重className过滤器（任一过滤器满足即可）
 */
public class OrNameFilters extends AbstractFilters<String> {

    public OrNameFilters(ClassNameFilter... matchers) {
        super(matchers);
    }

    public OrNameFilters(Collection<Filter<String>> filters) {
        super(filters);
    }

    @Override
    public boolean matches(String param) {
        return this.getFilters().stream().anyMatch(m -> m.matches(param));
    }
}
