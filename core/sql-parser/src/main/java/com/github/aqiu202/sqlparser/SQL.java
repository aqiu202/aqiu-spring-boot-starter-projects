package com.github.aqiu202.sqlparser;

import com.github.aqiu202.sqlparser.fragment.ExpressionHandler;
import net.sf.jsqlparser.statement.Statement;

import java.util.List;
import java.util.Set;

public interface SQL extends ExpressionHandler {

    List<? extends SQLFragment> getFragments();

    Statement getStatement();

    Set<String> findAllTableNames();

    default String asText() {
        return this.getStatement().toString();
    }

}
