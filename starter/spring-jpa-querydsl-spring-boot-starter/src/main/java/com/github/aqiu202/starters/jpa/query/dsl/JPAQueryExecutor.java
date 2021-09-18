package com.github.aqiu202.starters.jpa.query.dsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.List;

public interface JPAQueryExecutor extends JPQLQueryFactory {

    @Override
    JPADeleteClause delete(EntityPath<?> path);

    @Override
    <T> JPANAQuery<T> select(Expression<T> expr);

    @Override
    JPANAQuery<Tuple> select(Expression<?>... exprs);

    @Override
    <T> JPANAQuery<T> selectDistinct(Expression<T> expr);

    @Override
    JPANAQuery<Tuple> selectDistinct(Expression<?>... exprs);

    @Override
    JPANAQuery<Integer> selectOne();

    @Override
    JPANAQuery<Integer> selectZero();

    @Override
    <T> JPANAQuery<T> selectFrom(EntityPath<T> from);

    @Override
    JPANAQuery<?> from(EntityPath<?> from);

    @Override
    JPANAQuery<?> from(EntityPath<?>... from);

    @Override
    JPAUpdateClause update(EntityPath<?> path);

    @Override
    JPANAQuery<?> query();

    <T> T save(T entity);

    <T> T saveAndFlush(T entity);

    <T> List<T> save(Iterable<T> entities);
}
