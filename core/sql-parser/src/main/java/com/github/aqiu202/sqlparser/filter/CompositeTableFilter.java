package com.github.aqiu202.sqlparser.filter;

import com.github.aqiu202.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

public class CompositeTableFilter implements TableFilter {

    private List<TableFilter> filters;

    public CompositeTableFilter(TableFilter... tableFilters) {
        this(Arrays.asList(tableFilters));
    }

    public CompositeTableFilter(List<TableFilter> filters) {
        this.filters = filters;
    }

    public List<TableFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<TableFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean support(String tableName) {
        List<TableFilter> filters = this.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return true;
        }
        return filters.stream().allMatch(filter -> filter.support(tableName));
    }

    @Override
    public boolean support(String tableName, String tableFieldName) {
        List<TableFilter> filters = this.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return true;
        }
        return filters.stream().allMatch(filter -> filter.support(tableName, tableFieldName));
    }
}
