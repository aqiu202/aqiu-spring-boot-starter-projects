package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.statement.delete.Delete;

import java.util.Collections;

public class DeleteSQL extends AbstractSQL {
    public DeleteSQL(Delete statement) {
        super(statement, Collections.singletonList(new DeleteSQLFragment(statement)));
    }
}
