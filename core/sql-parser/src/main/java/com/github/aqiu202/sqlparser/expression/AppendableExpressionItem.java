package com.github.aqiu202.sqlparser.expression;

public interface AppendableExpressionItem {

    ExpressionItem getExpression();

    StringValueProvider getExpressionValueProvider();

    default String toExpression() {
        return this.getExpression().toExpression(this.getExpressionValueProvider().get());
    }
}
