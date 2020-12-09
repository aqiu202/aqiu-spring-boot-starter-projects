package com.github.aqiu202.starters.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public interface AnonymousPredicateBuilder<T> {

    Predicate build(Root<T> root, CriteriaBuilder cb);
}
