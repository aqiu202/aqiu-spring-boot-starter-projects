package com.github.aqiu202.sqlparser.fragment;

import com.github.aqiu202.sqlparser.util.SQLUtils;
import com.github.aqiu202.util.StringUtils;
import net.sf.jsqlparser.expression.Expression;

public interface ExpressionAppendable {
    default Expression createExpression(String expression) {
        if (StringUtils.isBlank(expression)) {
            return null;
        }
        return SQLUtils.createExpression(expression);
    }

    void appendExpression(Expression expression);
}
