package com.github.aqiu202.sqlparser.expression;

public class TextExpressionItem implements ExpressionItem {

    public static final TextExpressionItem INSTANCE = new TextExpressionItem();
    @Override
    public String getExpressionTemplate() {
        return "%s%s";
    }

    @Override
    public String getTableFieldName() {
        return "";
    }

}
