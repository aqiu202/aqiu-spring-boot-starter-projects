package com.github.aqiu202.sqlparser.expression;

public interface ExpressionAppendItem {

    ExpressionItem getExpression();

    StringValueProvider getExpressionValueProvider();

    default String toExpression() {
        return this.getExpression().toExpression(this.getExpressionValueProvider().get());
    }
}
