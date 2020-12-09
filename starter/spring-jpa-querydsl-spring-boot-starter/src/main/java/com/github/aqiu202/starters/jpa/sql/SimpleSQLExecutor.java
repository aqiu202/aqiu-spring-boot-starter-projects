package com.github.aqiu202.starters.jpa.sql;

import java.util.Map;
import javax.inject.Provider;
import javax.persistence.EntityManager;

public class SimpleSQLExecutor implements SQLExecutor {

    private final Provider<EntityManager> entityManager;

    public SimpleSQLExecutor(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
    }

    public SimpleSQLExecutor(EntityManager entityManager) {
        this.entityManager = () -> entityManager;
    }

    @Override
    public AbstractQuery<?> of(String sql, Map<String, Object> parameters) {
        return NativeSqlQuery.of(this.entityManager.get()).init(sql, parameters);
    }

    @Override
    public AbstractQuery<?> of(String sql, Object... parameters) {
        return NativeSqlQuery.of(this.entityManager.get()).init(sql, parameters);
    }

    /**
     * 构造HQL查询
     *
     * @author aqiu
     * @deprecated 不推荐使用
     */
    @Override
    public AbstractQuery<?> hql(String hql, Map<String, Object> parameters) {
        return HqlQuery.of(this.entityManager.get()).init(hql, parameters);
    }

    /**
     * 构造HQL查询
     *
     * @author aqiu
     * @deprecated 不推荐使用
     */
    @Override
    public AbstractQuery<?> hql(String hql, Object... parameters) {
        return HqlQuery.of(this.entityManager.get()).init(hql, parameters);
    }

    @Override
    public void clear() {
        this.entityManager.get().clear();
    }

}
