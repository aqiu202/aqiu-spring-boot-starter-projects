package com.github.aqiu202.sqlparser.expression;

public class TextAppendableExpressionItem implements AppendableExpressionItem {

    private final String expressionText;

    public TextAppendableExpressionItem(String expressionText) {
        this.expressionText = expressionText;
    }

    @Override
    public ExpressionItem getExpression() {
        return TextExpressionItem.INSTANCE;
    }

    @Override
    public StringValueProvider getExpressionValueProvider() {
        return () -> this.expressionText;
    }

}
