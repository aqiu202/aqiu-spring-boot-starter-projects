package com.github.aqiu202.sqlparser;

import net.sf.jsqlparser.statement.update.Update;

import java.util.Collections;

public class UpdateSQL extends AbstractSQL {

    public UpdateSQL(Update statement) {
        super(statement, Collections.singletonList(new UpdateSQLFragment(statement)));
    }
}
