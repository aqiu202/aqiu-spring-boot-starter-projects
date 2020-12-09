package com.github.aqiu202.starters.jpa.query.dsl;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import java.util.Map;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/25 22:21
 **/
public final class JPAProjections {

    public static <T> JPAQBeans<T> bean(Class<? extends T> type, Expression<?>... exprs) {
        return new JPAQBeans<>(type, exprs);
    }

    public static <T> JPAQBeans<T> bean(Path<? extends T> path, Expression<?>... exprs) {
        return new JPAQBeans<>(path.getType(), exprs);
    }

    public static <T> JPAQBeans<T> fields(Class<? extends T> type, Expression<?>... exprs) {
        return new JPAQBeans<T>(type, true, exprs);
    }

    public static <T> JPAQBeans<T> fields(Path<? extends T> type, Expression<?>... exprs) {
        return new JPAQBeans<T>(type.getType(), true, exprs);
    }

    public static <T> JPAQBeans<T> fields(Path<? extends T> type,
            Map<String, ? extends Expression<?>> bindings) {
        return new JPAQBeans<T>(type.getType(), true, bindings);
    }

    public static <T> JPAQBeans<T> fields(Class<? extends T> type,
            Map<String, ? extends Expression<?>> bindings) {
        return new JPAQBeans<T>(type, true, bindings);
    }

    public static JPAQMaps map(Expression<?>... exprs) {
        return new JPAQMaps(exprs);
    }

    public static JPAQTuple tuple(Expression<?>... exprs) {
        return new JPAQTuple(exprs);
    }

    public static JPAQTuple tuple(ImmutableList<Expression<?>> exprs) {
        return new JPAQTuple(exprs);
    }

    public static JPAQTuple tuple(Expression<?>[]... exprs) {
        return new JPAQTuple(exprs);
    }

}
