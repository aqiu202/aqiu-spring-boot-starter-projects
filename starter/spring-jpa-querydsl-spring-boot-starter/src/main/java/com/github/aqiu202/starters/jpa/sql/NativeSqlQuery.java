package com.github.aqiu202.starters.jpa.sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * 自定义SQL查询封装
 * @author AQIU 2018/11/26 10:26 AM
 **/
public final class NativeSqlQuery<T> extends AbstractQuery<T> {

    private NativeSqlQuery(EntityManager entityManager) {
        super(entityManager);
    }

    public static <T> NativeSqlQuery<T> of(EntityManager entityManager) {
        return new NativeSqlQuery<>(entityManager);
    }

    @Override
    public Query createQuery(String sql) {
        return this.entityManager.createNativeQuery(sql);
    }

    @Override
    protected String getCountSql(String sql) {
        return "SELECT count(*) FROM (" + sql + ")";
    }
}
