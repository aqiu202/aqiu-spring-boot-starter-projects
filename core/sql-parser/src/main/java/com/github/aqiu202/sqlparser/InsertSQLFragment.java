package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.insert.Insert;

import java.util.Collections;

public class InsertSQLFragment extends AbstractSQLFragment<Insert> {

    private final Insert insert;

    public InsertSQLFragment(Insert insert) {
        super(insert.getTable(), Collections.emptyList(), null);
        this.insert = insert;
    }

    @Override
    protected Insert getStatement() {
        return this.insert;
    }

    @Override
    public void appendExpression(Expression expression) {

    }
}
