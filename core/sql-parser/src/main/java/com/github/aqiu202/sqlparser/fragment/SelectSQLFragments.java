package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.SelectSQLFragment;
import com.github.aqiu202.sqlparser.expression.AppendableExpressionFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectSQLFragments implements ExpressionHandler {

    private final List<SelectSQLFragment> sqlFragments;

    public SelectSQLFragments(SelectSQLFragment sqlFragment) {
        this(Collections.singletonList(sqlFragment));
    }

    public SelectSQLFragments(List<SelectSQLFragment> sqlFragments) {
        this.sqlFragments = sqlFragments;
    }

    public Set<String> findAllTableNames() {
        Set<String> results = new HashSet<>();
        for (SelectSQLFragment ss : this.sqlFragments) {
            results.addAll(ss.findAllTableNames());
        }
        return results;
    }

    @Override
    public void accept(AppendableExpressionFactory<?> factory) {
        for (SelectSQLFragment ss : this.sqlFragments) {
            ss.accept(factory);
        }
    }
}
