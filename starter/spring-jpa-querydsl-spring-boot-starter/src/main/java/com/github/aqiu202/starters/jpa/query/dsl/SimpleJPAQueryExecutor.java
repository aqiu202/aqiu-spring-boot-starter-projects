package com.github.aqiu202.starters.jpa.query.dsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.dml.InsertClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAInsertClause;
import com.querydsl.jpa.impl.JPAUpdateClause;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class SimpleJPAQueryExecutor implements JPAQueryExecutor {

    @Nullable
    private final JPQLTemplates templates;

    private final Provider<EntityManager> entityManager;

    private final Map<String, EntityInformation<?, ?>> entityInformationMap = new HashMap<>();

    public SimpleJPAQueryExecutor(final EntityManager entityManager) {
        this.entityManager = () -> entityManager;
        this.templates = null;
    }

    public SimpleJPAQueryExecutor(@Nullable JPQLTemplates templates,
            final EntityManager entityManager) {
        this.entityManager = () -> entityManager;
        this.templates = templates;
    }

    public SimpleJPAQueryExecutor(Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
        this.templates = null;
    }

    public SimpleJPAQueryExecutor(@Nullable JPQLTemplates templates,
            Provider<EntityManager> entityManager) {
        this.entityManager = entityManager;
        this.templates = templates;
    }

    @Override
    public JPADeleteClause delete(EntityPath<?> path) {
        if (templates != null) {
            return new JPADeleteClause(entityManager.get(), path, templates);
        } else {
            return new JPADeleteClause(entityManager.get(), path);
        }
    }

    @Override
    public <T> JPANAQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public JPANAQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    @Override
    public <T> JPANAQuery<T> selectDistinct(Expression<T> expr) {
        return select(expr).distinct();
    }

    @Override
    public JPANAQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return select(exprs).distinct();
    }

    @Override
    public JPANAQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public JPANAQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public <T> JPANAQuery<T> selectFrom(EntityPath<T> from) {
        return select(from).from(from);
    }

    @Override
    public JPANAQuery<?> from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public JPANAQuery<?> from(EntityPath<?>... from) {
        return query().from(from);
    }

    @Override
    public JPAUpdateClause update(EntityPath<?> path) {
        if (templates != null) {
            return new JPAUpdateClause(entityManager.get(), path, templates);
        } else {
            return new JPAUpdateClause(entityManager.get(), path);
        }
    }

    public InsertClause<?> insert(EntityPath<?> path) {
        if (templates != null) {
            return new JPAInsertClause(entityManager.get(), path, templates);
        } else {
            return new JPAInsertClause(entityManager.get(), path);
        }
    }

    @Override
    public JPANAQuery<?> query() {
        if (templates != null) {
            return new JPANAQuery<Void>(entityManager.get(), templates);
        } else {
            return new JPANAQuery<Void>(entityManager.get());
        }
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public <T> T save(T entity) {
        Class<T> aClass = (Class<T>) entity.getClass();
        String classKey = aClass.getName();
        EntityInformation<T, ?> entityInformation;
        EntityManager entityManager = this.entityManager.get();
        if ((entityInformation = (EntityInformation<T, ?>) this.entityInformationMap.get(classKey))
                == null) {
            entityInformation = new JpaMetamodelEntityInformation<>(aClass,
                    this.entityManager.get().getMetamodel());
            synchronized (this.entityInformationMap) {
                this.entityInformationMap.put(classKey, entityInformation);
            }
        }
        if (entityInformation.isNew(entity)) {
            entityManager.persist(entity);
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    @Transactional
    public <T> T saveAndFlush(T entity) {
        T result = save(entity);
        this.entityManager.get().flush();
        return result;
    }

    @Transactional
    public <T> List<T> save(Iterable<T> entities) {
        List<T> result = new ArrayList<>();
        if (entities == null) {
            return result;
        }
        for (T entity : entities) {
            result.add(save(entity));
        }
        return result;
    }
}
