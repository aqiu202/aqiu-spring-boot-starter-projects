package com.github.aqiu202.starters.jpa.executor;

import com.github.aqiu202.starters.jpa.entity.KeyEntity;
import com.github.aqiu202.starters.jpa.operation.JpaDelete;
import com.github.aqiu202.starters.jpa.operation.JpaQuery;
import com.github.aqiu202.starters.jpa.operation.JpaUpdate;
import com.github.aqiu202.util.CollectionUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public class JpaExecutor {

    private final EntityManagerFactory entityManagerFactory;
    private final EntityManager entityManager;

    public JpaExecutor(EntityManagerFactory emf, EntityManager em) {
        this.entityManagerFactory = emf;
        this.entityManager = em;
    }

    @Transactional
    public void batchSave(Collection<?> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        for (Object entity : entities) {
            this.entityManager.persist(entity);
        }
    }

    @Transactional
    public void batchUpdate(Collection<?> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        for (Object entity : entities) {
            this.entityManager.merge(entity);
        }
    }

    @Transactional
    public void batchRemove(Collection<?> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return;
        }
        for (Object entity : entities) {
            this.entityManager.remove(entity);
        }
    }

    @Transactional
    public void persist(Object entity) {
        this.entityManager.persist(entity);
    }

    @Transactional
    public Object merge(Object entity) {
        return this.entityManager.merge(entity);
    }

    @Transactional
    public void remove(Object entity) {
        this.entityManager.remove(entity);
    }

    public <T> T findOne(Class<T> entityClass, Object primaryKey) {
        return this.entityManager.find(entityClass, primaryKey);
    }

    public <T extends KeyEntity> JpaQuery<T> forQuery(Class<T> entityClass) {
        return new JpaQuery<>(this.entityManager, entityClass);
    }

    public <T extends KeyEntity> JpaQuery<T> forQuery(EntityManager entityManager, Class<T> entityClass) {
        return new JpaQuery<>(entityManager, entityClass, false);
    }

    public <T extends KeyEntity> JpaUpdate<T> forUpdate(Class<T> entityClass) {
        return new JpaUpdate<>(this.entityManager, entityClass);
    }

    public <T extends KeyEntity> JpaDelete<T> forDelete(Class<T> entityClass) {
        return new JpaDelete<>(this.entityManager, entityClass);
    }

    public <T extends KeyEntity> JpaUpdate<T> forUpdate(EntityManager entityManager, Class<T> entityClass) {
        return new JpaUpdate<>(entityManager, entityClass, false);
    }

    public <T extends KeyEntity> JpaDelete<T> forDelete(EntityManager entityManager, Class<T> entityClass) {
        return new JpaDelete<>(entityManager, entityClass, false);
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public EntityManager buildNewEntityManager() {
        return this.entityManagerFactory.createEntityManager();
    }

    public void executeInTransaction(EntityTransaction transaction, Runnable runnable) {
        transaction.begin();
        try {
            runnable.run();
            // 提交事务
            transaction.commit();
        } catch (Exception e) {
            // 如果出现异常，回滚事务
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    public static class TransactionHolder {
        private static final ThreadLocal<EntityTransaction> transactions = new ThreadLocal<>();

        public static boolean hasTransaction() {
            return getTransaction() != null;
        }

        public static void setTransaction(EntityTransaction transaction) {
            transactions.set(transaction);
        }

        public static EntityTransaction getTransaction() {
            return transactions.get();
        }

        public static void removeTransaction() {
            transactions.remove();
        }

        public static void begin() {
            EntityTransaction transaction = getTransaction();
            if (transaction != null) {
                transaction.begin();
            }
        }

        public static void commit() {
            EntityTransaction transaction = getTransaction();
            if (transaction != null) {
                transaction.commit();
            }
        }

        public static void rollback() {
            EntityTransaction transaction = getTransaction();
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
        }

    }
}
