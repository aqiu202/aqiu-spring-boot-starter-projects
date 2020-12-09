package com.github.aqiu202.starters.jpa.dao;

import com.github.aqiu202.starters.jpa.sql.AbstractQuery;
import java.util.Map;

public interface SQLRepository {

    AbstractQuery of(String sql, Map<String, Object> parameters);

    AbstractQuery of(String sql, Object... parameters);

    AbstractQuery hql(String hql, Map<String, Object> parameters);

    AbstractQuery hql(String hql, Object... parameters);

    AbstractQuery of(SqlBuilder builder, Object... parameters);

    AbstractQuery of(SqlBuilder builder, Map<String, Object> parameters);

    AbstractQuery hql(SqlBuilder builder, Object... parameters);

    AbstractQuery hql(SqlBuilder builder, Map<String, Object> parameters);
}
