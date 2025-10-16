package com.github.aqiu202.sqlparser.expression;

public interface ExpressionItem {

    String getExpressionTemplate();

    String getTableFieldName();

    default String getVariableName() {
        return this.getTableFieldName();
    }

    default String getExpressionValuePlaceholder() {
        return String.format("${%s}", this.getVariableName());
    }

    default String toExpression() {
        return String.format(this.getExpressionTemplate(), this.getTableFieldName(), this.getExpressionValuePlaceholder());
    }

    default String toExpression(Object value) {
        return String.format(this.getExpressionTemplate(), this.getTableFieldName(), value);
    }
}
