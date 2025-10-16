package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.expression.AppendableExpressionFactory;

@FunctionalInterface
public interface ExpressionHandler {

    void accept(AppendableExpressionFactory<?> factory);
}
