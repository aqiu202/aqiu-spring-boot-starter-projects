package com.github.aqiu202.sqlparser.expression;

public class EqualsExpressionItem implements ExpressionItem {

    private static final String DEFAULT_TEMPLATE = "%s = %s";

    private final String expressionTemplate;
    private final String tableFieldName;
    private final String variableName;

    public EqualsExpressionItem(String expressionTemplate, String tableFieldName, String variableName) {
        this.expressionTemplate = expressionTemplate;
        this.tableFieldName = tableFieldName;
        this.variableName = variableName;
    }

    public EqualsExpressionItem(String tableFieldName, String variableName) {
        this(DEFAULT_TEMPLATE, tableFieldName, variableName);
    }

    public EqualsExpressionItem(String tableFieldName) {
        this(DEFAULT_TEMPLATE, tableFieldName, tableFieldName);
    }

    @Override
    public String getVariableName() {
        return variableName;
    }

    @Override
    public String getExpressionTemplate() {
        return expressionTemplate;
    }

    @Override
    public String getTableFieldName() {
        return this.tableFieldName;
    }
}
