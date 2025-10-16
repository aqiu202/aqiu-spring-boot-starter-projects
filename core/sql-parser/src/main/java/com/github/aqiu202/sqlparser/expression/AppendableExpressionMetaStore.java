package com.github.aqiu202.sqlparser.expression;


public interface AppendableExpressionMetaStore<M extends AppendableExpressionMeta> {

    M get(String tableName);

    void add(String tableName, M condition);
}
