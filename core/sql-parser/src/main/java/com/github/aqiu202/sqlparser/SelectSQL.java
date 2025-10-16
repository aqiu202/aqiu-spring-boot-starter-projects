package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.statement.Statement;

import java.util.List;

public class SelectSQL extends AbstractSQL {
    public SelectSQL(Statement statement, List<SelectSQLFragment> fragments) {
        super(statement, fragments);
    }
}
