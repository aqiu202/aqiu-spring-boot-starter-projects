package com.github.aqiu202.starters.jpa.operation;

import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.lambda.LambdaField;
import com.github.aqiu202.starters.jpa.predicate.PredicatesWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class JpaUpdate<T extends KeyEntity> extends PredicatesWrapper<JpaUpdate<T>, T> {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaUpdate<T> criteriaUpdate;
    private final Root<T> root;
    private final boolean autoClose;
    private EntityTransaction transaction;

    private final Map<String, Object> setValues = new LinkedHashMap<>();

    public JpaUpdate(EntityManager entityManager, Class<T> entityClass) {
        this(entityManager, entityClass, true);
    }

    public JpaUpdate(EntityManager entityManager, Class<T> entityClass, boolean autoClose) {
        this.entityManager = entityManager;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.criteriaUpdate = this.criteriaBuilder.createCriteriaUpdate(entityClass);
        this.root = this.criteriaUpdate.from(entityClass);
        this.autoClose = autoClose;
    }

    private void appendSetValues() {
        this.setValues.forEach((k, v) -> {
            Path path = this.buildPath(k);
            this.criteriaUpdate.set(path, v);
        });
    }

    private void appendPredicates() {
        this.criteriaUpdate.where(this.buildPredicates().toArray(new Predicate[0]));
    }

    public EntityTransaction getTransaction() {
        if (transaction == null) {
            this.transaction = this.entityManager.getTransaction();
        }
        return this.transaction;
    }

    public JpaUpdate<T> set(String path, Object value) {
        this.setValues.put(path, value);
        return this;
    }

    public <E> JpaUpdate<T> set(LambdaField<T, E> lambdaField, E value) {
        return this.set(this.resolvePath(lambdaField), value);
    }

    public int executeWithTransaction() {
        EntityTransaction transaction = this.getTransaction();
        transaction.begin();
        int results;
        try {
            results = this.doExecute();
            // 提交事务
            transaction.commit();
        } catch (Exception e) {
            // 如果出现异常，回滚事务
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            this.safeClose();
        }
        return results;
    }

    protected int doExecute() {
        this.appendSetValues();
        this.appendPredicates();
        return this.entityManager.createQuery(this.criteriaUpdate).executeUpdate();
    }

    public int execute() {
        try {
            return this.doExecute();
        } finally {
            this.safeClose();
        }
    }

    public void safeClose() {
        if (this.autoClose) {
            this.entityManager.close();
        }
    }

    @Override
    protected Root<?> getRoot() {
        return this.root;
    }

    @Override
    protected CriteriaBuilder getCriteriaBuilder() {
        return this.criteriaBuilder;
    }
}
