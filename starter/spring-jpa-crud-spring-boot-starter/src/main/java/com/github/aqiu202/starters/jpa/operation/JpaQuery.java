package com.github.aqiu202.starters.jpa.operation;

import com.github.aqiu202.page.PageResult;
import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.predicate.PredicatesWrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class JpaQuery<T extends KeyEntity> extends PredicatesWrapper<JpaQuery<T>, T> {

    private static final Logger log = LoggerFactory.getLogger(JpaQuery.class);
    private final EntityManager entityManager;
    private final Class<T> entityClass;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> criteriaQuery;
    private final Root<T> root;
    private final boolean autoClose;

    public JpaQuery(EntityManager entityManager, Class<T> entityClass) {
        this(entityManager, entityClass, true);
    }

    public JpaQuery(EntityManager entityManager, Class<T> entityClass, boolean autoClose) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.criteriaQuery = this.criteriaBuilder.createQuery(this.entityClass);
        this.root = this.criteriaQuery.from(this.entityClass);
        this.criteriaQuery.select(this.root);
        this.autoClose = autoClose;
    }

    @Override
    protected Root<?> getRoot() {
        return this.root;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder() {
        return this.criteriaBuilder;
    }

    private void appendPredicates() {
        this.appendPredicates(this.criteriaQuery);
    }

    private void appendPredicates(CriteriaQuery<?> query) {
        query.where(this.buildPredicates().toArray(new Predicate[0]));
    }

    private void appendPredicates(Root<?> root) {
        this.appendPredicates(root, this.criteriaQuery);
    }

    private void appendPredicates(Root<?> root, CriteriaQuery<?> query) {
        query.where(this.buildPredicates(root).toArray(new Predicate[0]));
    }

    public T queryOne(Object key) {
        return this.entityManager.find(this.entityClass, key);
    }

    public T queryOne() {
        try {
            this.appendPredicates();
            return this.entityManager.createQuery(this.criteriaQuery).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            this.safeClose();
        }
    }

    public long count() {
        try {
            CriteriaBuilder cb = this.criteriaBuilder;
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<T> countRoot = countQuery.from(this.entityClass);
            countQuery.select(cb.count(countRoot));
            this.appendPredicates(countRoot, countQuery);
            return this.entityManager.createQuery(countQuery).getSingleResult();
        } finally {
            this.safeClose();
        }
    }

    public boolean exists() {
        return this.count() > 0;
    }

    public List<? extends T> query() {
        try {
            this.appendPredicates();
            return this.entityManager.createQuery(this.criteriaQuery).getResultList();
        } finally {
            this.safeClose();
        }
    }

    public List<? extends T> limitQuery(int size) {
        try {
            this.appendPredicates();
            return this.entityManager.createQuery(this.criteriaQuery).setMaxResults(size).getResultList();
        } finally {
            this.safeClose();
        }
    }

    public PageResult<? extends T> pagingQuery(int offset, int size) {
        try {
            long total = this.count();
            // 如果总数为0，直接返回空列表，不再查询数据，减少服务器压力
            if (total == 0) {
                return PageResult.of(new ArrayList<>(), 0);
            }
            this.appendPredicates(this.criteriaQuery);
            TypedQuery<T> dataQuery = this.entityManager.createQuery(this.criteriaQuery);
            List<T> rows = dataQuery.setFirstResult(offset).setMaxResults(size).getResultList();
            return PageResult.of(rows, total);
        } finally {
            this.safeClose();
        }
    }

    public void safeClose() {
        if (this.autoClose) {
            this.entityManager.close();
        }
    }

}
