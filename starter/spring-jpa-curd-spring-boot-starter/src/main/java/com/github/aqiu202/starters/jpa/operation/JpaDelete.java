package com.github.aqiu202.starters.jpa.operation;

import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.predicate.PredicatesWrapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.Collection;

public class JpaDelete<T extends KeyEntity> extends PredicatesWrapper<JpaDelete<T>, T> {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaDelete<T> criteriaDelete;
    private final Root<T> root;
    private final boolean autoClose;
    private EntityTransaction transaction;

    public JpaDelete(EntityManager entityManager, Class<T> entityClass, boolean autoClose) {
        this.entityManager = entityManager;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.criteriaDelete = this.criteriaBuilder.createCriteriaDelete(entityClass);
        this.root = this.criteriaDelete.from(entityClass);
        this.autoClose = autoClose;
    }

    public JpaDelete(EntityManager entityManager, Class<T> entityClass) {
        this(entityManager, entityClass, true);
    }

    private void appendPredicates() {
        this.criteriaDelete.where(this.buildPredicates().toArray(new Predicate[0]));
    }

    public EntityTransaction getTransaction() {
        if (transaction == null) {
            this.transaction = this.entityManager.getTransaction();
        }
        return this.transaction;
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

    public int doExecute() {
        this.appendPredicates();
        return this.entityManager.createQuery(this.criteriaDelete).executeUpdate();
    }

    public int execute(Object key) {
        return this.andEqual(KeyEntity::getId, key).execute();
    }

    public int execute(Collection<String> keys) {
        return this.andIn(KeyEntity::getId, keys).execute();
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
