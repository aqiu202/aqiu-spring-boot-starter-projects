package com.github.aqiu202.starters.jpa.query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 * 自定义Specification实现用于辅助sql编程
 *
 * @author AQIU
 * @version 创建时间：2018年4月26日 下午6:03:08
 */
public class Spec<T> implements Specification<T> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private AnonymousPredicateBuilder<T> anonymousPredicateBuilder;

    private Spec(AnonymousPredicateBuilder<T> anonymousPredicateBuilder) {
        this.anonymousPredicateBuilder = anonymousPredicateBuilder;
    }

    public static <S> Spec<S> of(AnonymousPredicateBuilder<S> anonymousPredicateBuilder) {
        return new Spec<>(anonymousPredicateBuilder);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        Predicate p = anonymousPredicateBuilder.build(root, cb);
        return query.where(p).getRestriction();
    }

}
