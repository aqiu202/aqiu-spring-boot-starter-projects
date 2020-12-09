package com.github.aqiu202.starters.jpa.sql;

import java.util.Map;

public interface SQLExecutor extends ExtendsRepository, SQLRepository {

    AbstractQuery<?> of(String sql, Map<String, Object> parameters);

    AbstractQuery<?> of(String sql, Object... parameters);

    /**
     * 构造HQL查询
     *
     * @author aqiu
     * @deprecated 不推荐使用
     */
    AbstractQuery<?> hql(String hql, Map<String, Object> parameters);

    /**
     * 构造HQL查询
     *
     * @author aqiu
     * @deprecated 不推荐使用
     */
    AbstractQuery<?> hql(String hql, Object... parameters);

    void clear();

}
