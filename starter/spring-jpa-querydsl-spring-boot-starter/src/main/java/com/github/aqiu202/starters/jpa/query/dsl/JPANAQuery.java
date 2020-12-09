package com.github.aqiu202.starters.jpa.query.dsl;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.AbstractJPAQuery;
import com.querydsl.jpa.impl.JPAProvider;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QSort;

/**
 * <pre>workplan</pre>
 *
 * @author aqiu 2020/10/26 10:32
 **/
public class JPANAQuery<T> extends AbstractJPAQuery<T, JPANAQuery<T>> {

    public JPANAQuery() {
        super(null, JPQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    public JPANAQuery(EntityManager em) {
        super(em, JPAProvider.getTemplates(em), new DefaultQueryMetadata());
    }

    public JPANAQuery(EntityManager em, QueryMetadata metadata) {
        super(em, JPAProvider.getTemplates(em), metadata);
    }

    public JPANAQuery(EntityManager em, JPQLTemplates templates) {
        super(em, templates, new DefaultQueryMetadata());
    }

    public JPANAQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
        super(em, templates, metadata);
    }

    @Override
    public JPANAQuery<T> clone(EntityManager entityManager, JPQLTemplates templates) {
        JPANAQuery<T> q = new JPANAQuery<>(entityManager, templates, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public JPANAQuery<T> clone(EntityManager entityManager) {
        return clone(entityManager, JPAProvider.getTemplates(entityManager));
    }

    public JPANAQuery<T> orderBy(@Nonnull QSort sort) {
        return this.queryMixin.orderBy(sort.getOrderSpecifiers().toArray(new OrderSpecifier<?>[0]));
    }

    public JPANAQuery<T> pageable(Pageable pageable) {
        JPANAQuery<T> result = this.queryMixin.offset(pageable.getOffset())
                .limit(pageable.getPageSize());
        Sort sort = pageable.getSort();
        if (sort instanceof QSort) {
            return result.orderBy((QSort) sort);
        }
        return result;
    }

    public <U> JPANAQuery<U> select(Expression<U> expr) {
        this.queryMixin.setProjection(expr);
        return (JPANAQuery<U>) this;
    }

    public JPANAQuery<Tuple> select(Expression<?>... exprs) {
        this.queryMixin.setProjection(exprs);
        return (JPANAQuery<Tuple>) this;
    }

}
