package com.github.aqiu202.starters.jpa.query;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public interface AnonymousPredicateBuilder<T> {

    Predicate build(Root<T> root, CriteriaBuilder cb);
}
