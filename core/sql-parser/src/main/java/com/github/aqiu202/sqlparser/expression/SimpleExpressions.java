package com.github.aqiu202.sqlparser.expression;

import java.util.List;

public class SimpleExpressions implements AppendableExpressionMeta {

    private final List<AppendableExpressionItem> items;

    public SimpleExpressions(List<AppendableExpressionItem> items) {
        this.items = items;
    }

    public List<AppendableExpressionItem> getItems() {
        return items;
    }
}
