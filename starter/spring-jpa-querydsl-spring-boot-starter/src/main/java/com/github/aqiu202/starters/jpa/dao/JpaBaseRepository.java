package com.github.aqiu202.starters.jpa.dao;

import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface JpaBaseRepository<T, ID> extends JpaRepositoryImplementation<T, ID>,
        QuerydslPredicateExecutor<T> {

}
