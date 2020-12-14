package com.github.aqiu202.starters.jpa.dao.impl;

import com.github.aqiu202.starters.jpa.dao.SqlBuilder;
import java.util.StringJoiner;

public class BaseSqlBuilder implements SqlBuilder {

    private final StringBuilder builder;

    private static final char empty = ' ';
    private static final char split = ',';
    private static final String splitStr = ",";

    private static final String HANDLE_STR_SELECT = "SELECT";
    private static final String HANDLE_STR_UPDATE = "UPDATE";
    private static final String HANDLE_STR_DELETE = "DELETE";

    private static final String SQL_STR_FROM = "FROM";
    private static final String SQL_STR_LEFT_JOIN = "LEFT JOIN";
    private static final String SQL_STR_RIGHT_JOIN = "RIGHT JOIN";
    private static final String SQL_STR_INNER_JOIN = "INNER JOIN";
    private static final String SQL_STR_WHERE = "WHERE";
    private static final String SQL_STR_ON = "ON";
    private static final String SQL_STR_AND = "AND";
    private static final String SQL_STR_OR = "OR";
    private static final String SQL_STR_AS = "AS";
    private static final String SQL_STR_HAVING = "HAVING";
    private static final String SQL_STR_GROUP_BY = "GROUP BY";
    private static final String SQL_STR_LIKE = "LIKE";
    private static final String SQL_STR_CONCAT = "CONCAT";
    private static final String SQL_STR_SET = "SET";
    private static final char SQL_STR_EQ = '=';
    private static final char SQL_STR_GT = '>';
    private static final char SQL_STR_LT = '<';
    private static final String SQL_STR_LIKE_DLI = "'%'";
    private static final char SQL_CHAR_LIKE_DLI = '%';
    private static final char SQL_CHAR_QUOT = '\'';
    private static final String SQL_STR_NEQ = "!=";
    private static final String SQL_STR_LE = "<=";
    private static final String SQL_STR_GE = ">=";
    private static final String SQL_STR_IN = "IN";
    private static final char SQL_STR_NAME_PARAM_PREFIX = ':';
    private static final char SQL_STR_INDEX_PARAM_PREFIX = '?';
    private static final String SQL_STR_NAME_PARAM_PREFIX_S = ":";
    private static final String SQL_STR_INDEX_PARAM_PREFIX_S = "?";

    private BaseSqlBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    public static SqlBuilder select(CharSequence... fields) {
        StringJoiner joiner = new StringJoiner(splitStr);
        for (CharSequence field : fields) {
            joiner.add(field);
        }
        return new BaseSqlBuilder(new StringBuilder(HANDLE_STR_SELECT)).append(joiner.toString());
    }

    public static SqlBuilder update(CharSequence table) {
        return new BaseSqlBuilder(new StringBuilder(HANDLE_STR_UPDATE)).append(table);
    }

    public static SqlBuilder deleteFrom(CharSequence table) {
        return new BaseSqlBuilder(new StringBuilder(HANDLE_STR_DELETE)).from(table);
    }

    @Override
    public SqlBuilder from(CharSequence table) {
        return this.append(SQL_STR_FROM).append(table);
    }

    @Override
    public SqlBuilder as(CharSequence alias) {
        return this.append(SQL_STR_AS).append(alias);
    }

    @Override
    public SqlBuilder leftJoin(CharSequence table) {
        return this.append(SQL_STR_LEFT_JOIN).append(table);
    }

    @Override
    public SqlBuilder rightJoin(CharSequence table) {
        return this.append(SQL_STR_RIGHT_JOIN).append(table);
    }

    @Override
    public SqlBuilder innerJoin(CharSequence table) {
        return this.append(SQL_STR_INNER_JOIN).append(table);
    }

    @Override
    public SqlBuilder on(CharSequence c) {
        return this.append(SQL_STR_ON).append(c);
    }

    @Override
    public SqlBuilder where(CharSequence c) {
        return this.append(SQL_STR_WHERE).append(c);
    }

    @Override
    public SqlBuilder having(CharSequence c) {
        return this.append(SQL_STR_HAVING).append(c);
    }

    @Override
    public SqlBuilder count(CharSequence c) {
        return this.append("count(").builder(c).builder(")");
    }

    @Override
    public SqlBuilder eq(CharSequence c) {
        return this.append(SQL_STR_EQ).append(c);
    }

    @Override
    public SqlBuilder neq(CharSequence c) {
        return this.append(SQL_STR_NEQ).append(c);
    }

    @Override
    public SqlBuilder gt(CharSequence c) {
        return this.append(SQL_STR_GT).append(c);
    }

    @Override
    public SqlBuilder lt(CharSequence c) {
        return this.append(SQL_STR_LT).append(c);
    }

    @Override
    public SqlBuilder ge(CharSequence c) {
        return this.append(SQL_STR_GE).append(c);
    }

    @Override
    public SqlBuilder le(CharSequence c) {
        return this.append(SQL_STR_LE).append(c);
    }

    @Override
    public SqlBuilder startWith(CharSequence c) {
        return this.append(SQL_STR_LIKE).append(SQL_CHAR_QUOT).builder(c).builder(SQL_CHAR_LIKE_DLI)
                .builder(SQL_CHAR_QUOT);
    }

    @Override
    public SqlBuilder endWith(CharSequence c) {
        return this.append(SQL_STR_LIKE).append(SQL_CHAR_QUOT).builder(SQL_CHAR_LIKE_DLI).builder(c)
                .builder(SQL_CHAR_QUOT);
    }

    @Override
    public SqlBuilder contains(CharSequence c) {
        return this.append(SQL_STR_LIKE).append(SQL_CHAR_QUOT).builder(SQL_CHAR_LIKE_DLI).builder(c)
                .builder(SQL_CHAR_LIKE_DLI).builder(SQL_CHAR_QUOT);
    }

    @Override
    public SqlBuilder eqParam(CharSequence c) {
        return this.append(SQL_STR_EQ).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder neqParam(CharSequence c) {
        return this.append(SQL_STR_NEQ).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder gtParam(CharSequence c) {
        return this.append(SQL_STR_GT).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder ltParam(CharSequence c) {
        return this.append(SQL_STR_LT).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder geParam(CharSequence c) {
        return this.append(SQL_STR_GE).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder leParam(CharSequence c) {
        return this.append(SQL_STR_LE).append(SQL_STR_NAME_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder startWithParam(CharSequence c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_NAME_PARAM_PREFIX_S + c, SQL_STR_LIKE_DLI));
    }

    @Override
    public SqlBuilder endWithParam(CharSequence c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_LIKE_DLI, SQL_STR_NAME_PARAM_PREFIX_S + c));
    }

    @Override
    public SqlBuilder containsParam(CharSequence c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_LIKE_DLI, SQL_STR_NAME_PARAM_PREFIX_S + c,
                        SQL_STR_LIKE_DLI));
    }

    private CharSequence buildConcat(CharSequence... params) {
        StringBuilder builder = new StringBuilder(SQL_STR_CONCAT).append("(");
        StringJoiner joiner = new StringJoiner(splitStr);
        for (CharSequence param : params) {
            joiner.add(param);
        }
        return builder.append(joiner.toString()).append(")");
    }

    @Override
    public SqlBuilder eqParam(int c) {
        return this.append(SQL_STR_EQ).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder neqParam(int c) {
        return this.append(SQL_STR_NEQ).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder gtParam(int c) {
        return this.append(SQL_STR_GT).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder ltParam(int c) {
        return this.append(SQL_STR_LT).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder geParam(int c) {
        return this.append(SQL_STR_GE).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder leParam(int c) {
        return this.append(SQL_STR_LE).append(SQL_STR_INDEX_PARAM_PREFIX).builder(c);
    }

    @Override
    public SqlBuilder startWithParam(int c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_INDEX_PARAM_PREFIX_S + c,
                        SQL_STR_LIKE_DLI));
    }

    @Override
    public SqlBuilder endWithParam(int c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_LIKE_DLI, SQL_STR_INDEX_PARAM_PREFIX_S + c));
    }

    @Override
    public SqlBuilder containsParam(int c) {
        return this.append(SQL_STR_LIKE)
                .append(buildConcat(SQL_STR_LIKE_DLI, SQL_STR_INDEX_PARAM_PREFIX_S + c,
                        SQL_STR_LIKE_DLI));
    }

    /**
     * Oracle 不能使用，因为Oracle的concat函数仅支持两个参数
     * @param cs params
     */
    @Override
    public SqlBuilder concat(CharSequence... cs) {
        StringJoiner joiner = new StringJoiner(splitStr);
        for (CharSequence c : cs) {
            joiner.add(c);
        }
        return this.append(SQL_STR_CONCAT).builder("(").builder(joiner.toString()).builder(")");
    }

    @Override
    public SqlBuilder round(CharSequence c) {
        return this.append("ROUND(" + c + ")");
    }

    @Override
    public SqlBuilder ceil(CharSequence c) {
        return this.append("CEIL(" + c + ")");
    }

    @Override
    public SqlBuilder floor(CharSequence c) {
        return this.append("FLOOR(" + c + ")");
    }

    @Override
    public SqlBuilder mod(CharSequence c) {
        return this.append("MOD(" + c + ")");
    }

    @Override
    public SqlBuilder min(CharSequence c) {
        return this.append("MIN(" + c + ")");
    }

    @Override
    public SqlBuilder max(CharSequence c) {
        return this.append("MAX(" + c + ")");
    }

    @Override
    public SqlBuilder dateFormat(CharSequence field, String pattern) {
        return this.append("DATE_TIME(").builder(field).builder(split).append(pattern).append(")");
    }

    @Override
    public SqlBuilder cast(CharSequence field, String targetType) {
        return this.append("CAST(").builder(field).as(targetType).builder(")");
    }

    @Override
    public SqlBuilder fun(String name, CharSequence... params) {
        StringJoiner joiner = new StringJoiner(splitStr);
        for (CharSequence c : params) {
            joiner.add(c);
        }
        return this.append(name).builder("(").builder(joiner.toString()).builder((")"));
    }

    @Override
    public SqlBuilder set(CharSequence field) {
        return this.append(SQL_STR_SET).append(field);
    }

    @Override
    public SqlBuilder groupBy(CharSequence... fields) {
        StringJoiner joiner = new StringJoiner(splitStr);
        for (CharSequence field : fields) {
            joiner.add(field);
        }
        return this.append(SQL_STR_GROUP_BY).append(joiner.toString());
    }

    @Override
    public SqlBuilder and(CharSequence c) {
        return this.append(SQL_STR_AND).append(c);
    }

    @Override
    public SqlBuilder or(CharSequence c) {
        return this.append(SQL_STR_OR).append(c);
    }

    @Override
    public SqlBuilder append(Object c) {
        this.builder.append(empty).append(c);
        return this;
    }

    @Override
    public SqlBuilder builder(Object c) {
        this.builder.append(c);
        return this;
    }
//    @Override
//    public SqlBuilder delete(CharSequence table) {
//        return null;
//    }

    @Override
    public SqlBuilder addParam(int index) {
        return this.append(SQL_STR_INDEX_PARAM_PREFIX).builder(index);
    }

    @Override
    public SqlBuilder addParam(String name) {
        return this.append(SQL_STR_NAME_PARAM_PREFIX).builder(name);
    }

    @Override
    public SqlBuilder em() {
        return this.builder(empty);
    }

    @Override
    public SqlBuilder sp() {
        return this.builder(split);
    }

    @Override
    public SqlBuilder in(CharSequence... cs) {
        return this.fun(SQL_STR_IN, cs);
    }

    @Override
    public SqlBuilder inParam(CharSequence c) {
        return this.fun(SQL_STR_IN, SQL_STR_NAME_PARAM_PREFIX_S + c);
    }

    @Override
    public SqlBuilder inParam(int c) {
        return this.fun(SQL_STR_IN, SQL_STR_INDEX_PARAM_PREFIX_S + c);
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }

    @Override
    public String build() {
        return this.builder.toString();
    }

}
