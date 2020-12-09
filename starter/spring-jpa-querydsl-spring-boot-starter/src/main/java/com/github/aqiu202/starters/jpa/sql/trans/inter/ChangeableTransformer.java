package com.github.aqiu202.starters.jpa.sql.trans.inter;

import org.hibernate.transform.ResultTransformer;

public interface ChangeableTransformer<T> extends ResultTransformer {

    <S> ChangeableTransformer<S> as(Class<S> newResultClass);

    Class<T> getResultClass();
}
