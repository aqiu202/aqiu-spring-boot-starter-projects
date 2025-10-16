package com.github.aqiu202.sqlparser.fragment;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Table;

public class TableFragment {

    private final Table table;

    public TableFragment(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return this.table;
    }

    public String getTableName() {
        return this.table == null ? null : this.table.getName();
    }

    public String getTableAlias() {
        if (this.table == null) {
            return null;
        }
        Alias alias = this.table.getAlias();
        return alias == null ? null : alias.getName();
    }
}
