package com.github.aqiu202.starters.jpa.sql;


import com.github.aqiu202.starters.jpa.sql.trans.BeanTransformerAdapter;
import com.github.aqiu202.starters.jpa.sql.trans.MapBeanTransformer;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.hibernate.query.spi.QueryImplementor;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Pageable;

@SuppressWarnings({"unchecked"})
public class BaseQuery<T> {

    protected Query query;
    protected Query countQuery;
    protected ResultTransformer mapTransformer = MapBeanTransformer.INSTANCE;
    protected BeanTransformerAdapter<?> beanTransformer;
    protected Pageable pageable;

    protected BaseQuery() {
    }

    protected BaseQuery(BeanTransformerAdapter<?> changeableTransformer) {
        this.beanTransformer = changeableTransformer;
    }

    protected void pageable(Pageable pageable) {
        this.pageable = pageable;
    }

    protected T one() {
        if (beanTransformer != null) {
            query.unwrap(QueryImplementor.class).setResultTransformer(beanTransformer);
        }
        return (T) this.object();
    }

    protected List<T> list() {
        if (beanTransformer != null) {
            query.unwrap(QueryImplementor.class).setResultTransformer(beanTransformer);
        }
        this.handlePageable();
        return (List<T>) query.getResultList();
    }

    protected List<Map<String, Object>> humpMapList() {
        query.unwrap(QueryImplementor.class).setResultTransformer(mapTransformer);
        this.handlePageable();
        return (List<Map<String, Object>>) query.getResultList();
    }

    protected Map<String, Object> humpMap() {
        query.unwrap(QueryImplementor.class).setResultTransformer(mapTransformer);
        return (Map<String, Object>) this.object();
    }

    protected List<Map<String, Object>> mapList() {
        query.unwrap(QueryImplementor.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        this.handlePageable();
        return (List<Map<String, Object>>) query.getResultList();
    }

    protected Map<String, Object> map() {
        query.unwrap(QueryImplementor.class)
            .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return (Map<String, Object>) this.object();
    }

    protected List<Object> objects() {
        this.handlePageable();
        return (List<Object>) query.getResultList();
    }

    protected Object object() {
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    protected long total() {
        Object result = countQuery.getSingleResult();
        if (result instanceof Long) {
            return (long) result;
        }
        BigInteger bigint = (BigInteger) result;
        return bigint == null ? 0L : bigint.longValue();
    }

    protected void handlePageable() {
        if (this.pageable != null) {
            this.query.setFirstResult(Long.valueOf(this.pageable.getOffset()).intValue());
            this.query.setMaxResults(this.pageable.getPageSize());
        }
    }
}
