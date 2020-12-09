package com.github.aqiu202.starters.jpa.dao.impl;

import com.github.aqiu202.starters.jpa.dao.JpaBaseRepository;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;


public class JpaBaseRepositoryImpl<T, ID> extends SimpleJpaRepository<T, ID> implements
        JpaBaseRepository<T, ID> {

    private final QuerydslJpaPredicateExecutor<T> querydslJpaPredicateExecutor;

    public JpaBaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.querydslJpaPredicateExecutor = new QuerydslJpaPredicateExecutor<>(entityInformation,
                entityManager,
                SimpleEntityPathResolver.INSTANCE, null);
    }

    @Nonnull
    @Override
    public Optional<T> findOne(@Nonnull Predicate predicate) {
        return querydslJpaPredicateExecutor.findOne(predicate);
    }

    @Nonnull
    @Override
    public Iterable<T> findAll(@Nonnull Predicate predicate) {
        return querydslJpaPredicateExecutor.findAll(predicate);
    }

    @Nonnull
    @Override
    public Iterable<T> findAll(@Nonnull Predicate predicate, @Nonnull Sort sort) {
        return querydslJpaPredicateExecutor.findAll(predicate, sort);
    }

    @Nonnull
    @Override
    public Iterable<T> findAll(@Nonnull Predicate predicate, @Nonnull OrderSpecifier<?>... orders) {
        return querydslJpaPredicateExecutor.findAll(predicate, orders);
    }

    @Nonnull
    @Override
    public Iterable<T> findAll(@Nonnull OrderSpecifier<?>... orders) {
        return querydslJpaPredicateExecutor.findAll(orders);
    }

    @Nonnull
    @Override
    public Page<T> findAll(@Nonnull Predicate predicate, @Nonnull Pageable pageable) {
        return querydslJpaPredicateExecutor.findAll(predicate, pageable);
    }

    @Override
    public long count(@Nonnull Predicate predicate) {
        return querydslJpaPredicateExecutor.count(predicate);
    }

    @Override
    public boolean exists(@Nonnull Predicate predicate) {
        return querydslJpaPredicateExecutor.exists(predicate);
    }
}
