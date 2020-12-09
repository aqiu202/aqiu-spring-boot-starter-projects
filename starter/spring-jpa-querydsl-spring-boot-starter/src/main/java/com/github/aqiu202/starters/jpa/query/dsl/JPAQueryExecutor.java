package com.github.aqiu202.starters.jpa.query.dsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;

public interface JPAQueryExecutor extends JPQLQueryFactory {

    JPADeleteClause delete(EntityPath<?> path);

    <T> JPANAQuery<T> select(Expression<T> expr);

    JPANAQuery<Tuple> select(Expression<?>... exprs);

    <T> JPANAQuery<T> selectDistinct(Expression<T> expr);

    JPANAQuery<Tuple> selectDistinct(Expression<?>... exprs);

    JPANAQuery<Integer> selectOne();

    JPANAQuery<Integer> selectZero();

    <T> JPANAQuery<T> selectFrom(EntityPath<T> from);

    JPANAQuery<?> from(EntityPath<?> from);

    JPANAQuery<?> from(EntityPath<?>... from);

    JPAUpdateClause update(EntityPath<?> path);

    JPANAQuery<?> query();

    <T> T save(T entity);

    <T> T saveAndFlush(T entity);

    <T> List<T> save(Iterable<T> entities);
}
