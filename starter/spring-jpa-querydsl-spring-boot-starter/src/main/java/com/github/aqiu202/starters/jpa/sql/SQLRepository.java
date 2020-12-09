package com.github.aqiu202.starters.jpa.sql;

import java.util.Map;

public interface SQLRepository {

    AbstractQuery of(String sql, Map<String, Object> parameters);

    AbstractQuery of(String sql, Object... parameters);

    AbstractQuery hql(String hql, Map<String, Object> parameters);

    AbstractQuery hql(String hql, Object... parameters);

}
