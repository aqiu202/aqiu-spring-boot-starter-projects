package com.github.aqiu202.sqlparser.expression;

public class SimpleExpressionAppendItem implements ExpressionAppendItem {

    private final ExpressionItem expression;

    private final StringValueProvider expressionValueProvider;

    public SimpleExpressionAppendItem(ExpressionItem expression, StringValueProvider expressionValueProvider) {
        this.expression = expression;
        this.expressionValueProvider = expressionValueProvider;
    }

    @Override
    public ExpressionItem getExpression() {
        return expression;
    }

    @Override
    public StringValueProvider getExpressionValueProvider() {
        return expressionValueProvider;
    }
}
