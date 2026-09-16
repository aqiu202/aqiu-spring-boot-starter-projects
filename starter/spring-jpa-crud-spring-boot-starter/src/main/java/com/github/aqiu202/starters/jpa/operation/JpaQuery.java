package com.github.aqiu202.starters.jpa.operation;

import com.github.aqiu202.page.PageParam;
import com.github.aqiu202.page.PageResult;
import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.lambda.LambdaField;
import com.github.aqiu202.starters.jpa.predicate.PredicatesWrapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
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

    private final List<Order> orders = new ArrayList<>();
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

    private void appendPredicates(CriteriaQuery<?> query) {
        query.where(this.buildPredicates().toArray(new Predicate[0]));
    }

    private void appendOrders(CriteriaQuery<?> query) {
        query.orderBy(this.orders);
    }

    private void appendPredicates(Root<?> root, CriteriaQuery<?> query) {
        query.where(this.buildPredicates(root).toArray(new Predicate[0]));
    }

    private JpaQuery<T> orderBy(Order order) {
        this.orders.add(order);
        return this;
    }

    public JpaQuery<T> orderByAsc(String path) {
        CriteriaBuilder criteriaBuilder = this.getCriteriaBuilder();
        return this.orderBy(criteriaBuilder.asc(root.get(path)));
    }

    public JpaQuery<T> orderByDesc(String path) {
        CriteriaBuilder criteriaBuilder = this.getCriteriaBuilder();
        return this.orderBy(criteriaBuilder.desc(root.get(path)));
    }

    public JpaQuery<T> orderByAsc(LambdaField<T, ?> field) {
        return this.orderByAsc(this.resolvePath(field));
    }

    public JpaQuery<T> orderByDesc(LambdaField<T, ?> field) {
        return this.orderByDesc(this.resolvePath(field));
    }

    public T queryOne(Object key) {
        return this.entityManager.find(this.entityClass, key);
    }

    public T queryOne() {
        try {
            CriteriaQuery<T> query = this.criteriaQuery;
            this.appendPredicates(query);
            return this.entityManager.createQuery(query).getSingleResult();
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
            CriteriaQuery<T> query = this.criteriaQuery;
            this.appendPredicates(query);
            this.appendOrders(query);
            return this.entityManager.createQuery(query).getResultList();
        } finally {
            this.safeClose();
        }
    }

    public List<? extends T> limitQuery(int size) {
        try {
            CriteriaQuery<T> query = this.criteriaQuery;
            this.appendPredicates(query);
            this.appendOrders(query);
            return this.entityManager.createQuery(query).setMaxResults(size).getResultList();
        } finally {
            this.safeClose();
        }
    }

    public PageResult<? extends T> pagingQuery(PageParam pageParam) {
        return this.pagingQuery((int) pageParam.getOffset(), pageParam.getPageSize());
    }

    public PageResult<? extends T> pagingQuery(int offset, int size) {
        try {
            long total = this.count();
            // 如果总数为0，直接返回空列表，不再查询数据，减少服务器压力
            if (total == 0) {
                return PageResult.of(new ArrayList<>(), 0);
            }
            CriteriaQuery<T> query = this.criteriaQuery;
            this.appendPredicates(query);
            this.appendOrders(query);
            TypedQuery<T> dataQuery = this.entityManager.createQuery(query);
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
