package com.github.aqiu202.starters.jpa.dao;

public interface SqlBuilder {

    SqlBuilder from(CharSequence table);

    SqlBuilder as(CharSequence alias);

    SqlBuilder leftJoin(CharSequence table);

    SqlBuilder rightJoin(CharSequence table);

    SqlBuilder innerJoin(CharSequence table);

    SqlBuilder on(CharSequence c);

    SqlBuilder where(CharSequence c);

    SqlBuilder having(CharSequence c);

    SqlBuilder count(CharSequence c);

    SqlBuilder eq(CharSequence c);

    SqlBuilder neq(CharSequence c);

    SqlBuilder gt(CharSequence c);

    SqlBuilder lt(CharSequence c);

    SqlBuilder ge(CharSequence c);

    SqlBuilder le(CharSequence c);

    SqlBuilder in(CharSequence... c);

    SqlBuilder startWith(CharSequence c);

    SqlBuilder endWith(CharSequence c);

    SqlBuilder contains(CharSequence c);

    SqlBuilder eqParam(CharSequence c);

    SqlBuilder neqParam(CharSequence c);

    SqlBuilder gtParam(CharSequence c);

    SqlBuilder ltParam(CharSequence c);

    SqlBuilder geParam(CharSequence c);

    SqlBuilder leParam(CharSequence c);

    SqlBuilder startWithParam(CharSequence c);

    SqlBuilder endWithParam(CharSequence c);

    SqlBuilder containsParam(CharSequence c);

    SqlBuilder inParam(CharSequence c);

    SqlBuilder eqParam(int c);

    SqlBuilder neqParam(int c);

    SqlBuilder gtParam(int c);

    SqlBuilder ltParam(int c);

    SqlBuilder geParam(int c);

    SqlBuilder leParam(int c);

    SqlBuilder startWithParam(int c);

    SqlBuilder endWithParam(int c);

    SqlBuilder containsParam(int c);

    SqlBuilder inParam(int c);

    SqlBuilder concat(CharSequence... c);

    SqlBuilder round(CharSequence c);

    SqlBuilder ceil(CharSequence c);

    SqlBuilder floor(CharSequence c);

    SqlBuilder mod(CharSequence c);

    SqlBuilder min(CharSequence c);

    SqlBuilder max(CharSequence c);

    SqlBuilder dateFormat(CharSequence field, String pattern);

    SqlBuilder cast(CharSequence field, String targetType);

    SqlBuilder set(CharSequence field);

    SqlBuilder groupBy(CharSequence... fields);

    SqlBuilder and(CharSequence c);

    SqlBuilder or(CharSequence c);

    SqlBuilder fun(String name, CharSequence... params);

    SqlBuilder builder(Object o);

    SqlBuilder append(Object o);

    SqlBuilder addParam(int index);

    SqlBuilder addParam(String name);

    SqlBuilder em();

    SqlBuilder sp();

    String build();
}
