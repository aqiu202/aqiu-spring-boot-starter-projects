package com.github.aqiu202.sqlparser.expression;

public class StringEqualsExpressionItem extends EqualsExpressionItem {

    private static final String DEFAULT_TEMPLATE = "%s = '%s'";

    public StringEqualsExpressionItem(String tableFieldName) {
        super(DEFAULT_TEMPLATE, tableFieldName, tableFieldName);
    }
}
