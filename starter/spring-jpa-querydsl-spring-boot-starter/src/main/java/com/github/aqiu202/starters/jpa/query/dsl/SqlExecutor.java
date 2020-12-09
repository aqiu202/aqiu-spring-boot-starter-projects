package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.starters.jpa.dao.ExtendsRepository;
import com.github.aqiu202.starters.jpa.dao.SQLRepository;
import com.github.aqiu202.starters.jpa.dao.SqlBuilder;
import com.github.aqiu202.starters.jpa.sql.AbstractQuery;
import com.github.aqiu202.starters.jpa.sql.HqlQuery;
import com.github.aqiu202.starters.jpa.sql.NativeSqlQuery;
import java.util.Map;
import javax.persistence.EntityManager;

public class SqlExecutor implements ExtendsRepository, SQLRepository {

    private final EntityManager entityManager;

    public SqlExecutor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AbstractQuery<?> of(String sql, Map<String, Object> parameters) {
        return NativeSqlQuery.of(this.entityManager).init(sql, parameters);
    }

    @Override
    public AbstractQuery<?> of(String sql, Object... parameters) {
        return NativeSqlQuery.of(this.entityManager).init(sql, parameters);
    }

    /**
     * @author aqiu 不支持关联查询和子查询
     */
    @Override
    public AbstractQuery<?> hql(String hql, Map<String, Object> parameters) {
        return HqlQuery.of(this.entityManager).init(hql, parameters);
    }

    /**
     * @author aqiu 不支持关联查询和子查询
     */
    @Override
    public AbstractQuery<?> hql(String hql, Object... parameters) {
        return HqlQuery.of(this.entityManager).init(hql, parameters);
    }

    @Override
    public void clear() {
        this.entityManager.clear();
    }

    @Override
    public AbstractQuery<?> of(SqlBuilder builder, Object... parameters) {
        return of(builder.build(), parameters);
    }

    @Override
    public AbstractQuery<?> of(SqlBuilder builder, Map<String, Object> parameters) {
        return of(builder.build(), parameters);
    }

    @Override
    public AbstractQuery<?> hql(SqlBuilder builder, Object... parameters) {
        return hql(builder.build(), parameters);
    }

    @Override
    public AbstractQuery<?> hql(SqlBuilder builder, Map<String, Object> parameters) {
        return hql(builder.build(), parameters);
    }
}
