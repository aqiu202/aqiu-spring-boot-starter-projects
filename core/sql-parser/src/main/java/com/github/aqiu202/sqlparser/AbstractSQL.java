package com.github.aqiu202.sqlparser;

import com.github.aqiu202.sqlparser.expression.AppendableExpressionFactory;
import com.github.aqiu202.util.CollectionUtils;
import net.sf.jsqlparser.statement.Statement;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AbstractSQL implements SQL {

    private final Statement statement;
    private final List<? extends SQLFragment> fragments;

    public AbstractSQL(Statement statement, List<? extends SQLFragment> fragments) {
        this.statement = statement;
        this.fragments = fragments;
    }

    @Override
    public Statement getStatement() {
        return statement;
    }

    @Override
    public Set<String> findAllTableNames() {
        Set<String> results = new HashSet<>();
        if (CollectionUtils.isNotEmpty(fragments)) {
            for (SQLFragment fragment : fragments) {
                results.addAll(fragment.findAllTableNames());
            }
        }
        return results;
    }

    @Override
    public List<? extends SQLFragment> getFragments() {
        return fragments;
    }


    @Override
    public void accept(AppendableExpressionFactory<?> factory) {
        List<? extends SQLFragment> fragments = this.getFragments();
        for (SQLFragment fragment : fragments) {
            fragment.accept(factory);
        }
    }
}
