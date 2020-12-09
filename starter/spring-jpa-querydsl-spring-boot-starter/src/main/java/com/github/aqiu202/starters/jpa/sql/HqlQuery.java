package com.github.aqiu202.starters.jpa.sql;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public final class HqlQuery<T> extends AbstractQuery<T> {

    private HqlQuery(EntityManager entityManager) {
        super(entityManager);
    }

    public static <T> HqlQuery<T> of(EntityManager entityManager) {
        return new HqlQuery<>(entityManager);
    }

    @Override
    public Query createQuery(String hql) {
        return this.entityManager.createQuery(hql);
    }

    @Override
    protected String getCountSql(String sql) {
        int i = sql.toLowerCase().indexOf("from");
        if (i == -1) {
            return null;
        }
        StringBuilder sb = new StringBuilder(sql);
        return "select count(*) " + sb.substring(i);
    }
}
