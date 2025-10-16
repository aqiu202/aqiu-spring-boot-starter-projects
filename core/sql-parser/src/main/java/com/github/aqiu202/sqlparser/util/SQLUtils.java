package com.github.aqiu202.sqlparser.util;

import com.github.aqiu202.sqlparser.SQL;
import com.github.aqiu202.sqlparser.SQLParser;
import com.github.aqiu202.sqlparser.SimpleSQLParser;
import com.github.aqiu202.util.CollectionUtils;
import java.util.Collection;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SQLUtils {

    private static final Logger log = LoggerFactory.getLogger(SQLUtils.class);

    private static final SQLParser SQL_PARSER = SimpleSQLParser.INSTANCE;

    public static SQL parse(String sql) {
        return SQL_PARSER.parse(sql);
    }

    public static Expression createExpression(String expression) {
        try {
            return CCJSqlParserUtil.parseCondExpression(expression);
        } catch (JSQLParserException e) {
            log.error("创建表达式失败：", e);
        }
        return null;
    }

    public static Expression buildAndExpressions(List<Expression> expressions) {
        if (CollectionUtils.isEmpty(expressions)) {
            return null;
        }
        int size = expressions.size();
        Expression result = expressions.get(0);
        if (size == 1) {
            return result;
        }
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                continue;
            }
            Expression eexpression = expressions.get(i);
            result = new AndExpression(result, eexpression);
        }
        return result;
    }
}
