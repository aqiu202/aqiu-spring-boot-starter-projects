package com.github.aqiu202.starters.jpa.sql;

import com.github.aqiu202.starters.jpa.page.PageParam;
import com.github.aqiu202.starters.jpa.sql.trans.BeanTransformerAdapter;
import com.github.aqiu202.starters.jpa.sql.trans.SimpleTransformer;
import com.github.aqiu202.starters.jpa.sql.trans.inter.AnonymousResultTransformer;
import com.github.aqiu202.starters.jpa.util.JPAExpressionSingleton;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public abstract class AbstractQuery<T> extends BaseQuery<T> {

    protected final EntityManager entityManager;
    protected String sql;
    protected String countSql;
    protected Map<String, Object> parameters;
    protected Object[] params;
    private boolean isItemParams;
    private boolean isSorted = false;

    protected AbstractQuery(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public <S> AbstractQuery<S> as(Class<S> s) {
        if (this.beanTransformer == null) {
            this.beanTransformer = BeanTransformerAdapter.of(s);
        } else if (!s.equals(this.beanTransformer.getResultClass())) {
            this.beanTransformer = this.beanTransformer.as(s);
        }
        return (AbstractQuery<S>) this;
    }

    private AbstractQuery<T> init(String sql, boolean isItemParams) {
        //解析SpEL表达式
        this.sql = (String) JPAExpressionSingleton.getParser()
                .parseExpression(sql, JPAExpressionSingleton.getTemplate()).getValue();
        this.isItemParams = isItemParams;
        return this;
    }

    public AbstractQuery<T> init(String sql) {
        return this.init(sql, true);
    }

    public AbstractQuery<T> init(String sql, Map<String, Object> parameters) {
        this.init(sql, false);
        this.parameters = parameters;
        return this;
    }

    public AbstractQuery<T> init(String sql, Object... parameters) {
        this.init(sql, true);
        this.params = parameters;
        return this;
    }

    public AbstractQuery<T> countSql() {
        return this.countSql(this.getCountSql(this.sql));
    }

    public AbstractQuery<T> countSql(String countSql) {
        this.countSql = countSql;
        return this;
    }

    public Page<T> pagingList(Pageable pageable) {
        this.beforePagingQuery(pageable);
        List<T> list;
        long total = this.total();
        if (total > pageable.getOffset()) {
            list = this.list();
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, pageable, total);
    }

    public Page<T> pagingList(int page, int size, Sort sort) {
        return pagingList(PageParam.of(page, size, sort));
    }

    public Page<T> pagingList(int page, int size) {
        return pagingList(page, size, null);
    }

    public Page<Object> pagingObjects(Pageable pageable) {
        this.beforePagingQuery(pageable);
        long total = this.total();
        List<Object> list;
        if (total > pageable.getOffset()) {
            list = this.objects();
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, pageable, total);
    }

    public Page<Object> pagingObjects(int page, int size, Sort sort) {
        return pagingObjects(PageParam.of(page, size, sort));
    }

    public Page<Object> pagingObjects(int page, int size) {
        return pagingObjects(page, size, null);
    }

    public Page<Map<String, Object>> pagingMapList(Pageable pageable) {
        this.beforePagingQuery(pageable);
        List<Map<String, Object>> list;
        long total = this.total();
        if (total > pageable.getOffset()) {
            list = this.mapList();
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, pageable, total);
    }

    public Page<Map<String, Object>> pagingHumpMapList(Pageable pageable,
            AnonymousResultTransformer anonymousResultTransformer) {
        this.beforePagingQuery(pageable);
        if (anonymousResultTransformer != null) {
            this.mapTransformer = new SimpleTransformer(anonymousResultTransformer);
        }
        List<Map<String, Object>> list;
        long total = this.total();
        if (total > pageable.getOffset()) {
            list = this.humpMapList();
        } else {
            list = new ArrayList<>();
        }
        return new PageImpl<>(list, pageable, total);
    }

    public Page<Map<String, Object>> pagingMapList(int page, int size, Sort sort) {
        return pagingMapList(PageParam.of(page, size, sort));
    }

    public Page<Map<String, Object>> pagingMapList(int page, int size) {
        return pagingMapList(page, size, null);
    }

    public Page<Map<String, Object>> pagingHumpMapList(Pageable pageable) {
        return pagingHumpMapList(pageable, null);
    }

    public Page<Map<String, Object>> pagingHumpMapList(int page, int size, Sort sort) {
        return pagingHumpMapList(PageParam.of(page, size, sort), null);
    }

    public Page<Map<String, Object>> pagingHumpMapList(int page, int size, Sort sort,
            AnonymousResultTransformer anonymousResultTransformer) {
        return pagingHumpMapList(PageParam.of(page, size, sort), anonymousResultTransformer);
    }

    public List<T> queryList(Sort sort) {
        this.prepareListQuery(this.sql, sort);
        return this.list();
    }

    public List<T> queryList() {
        return queryList(null);
    }

    public List<Object> queryObjectList(Sort sort) {
        this.prepareListQuery(this.sql, sort);
        return this.objects();
    }

    public List<Object> queryObjectList() {
        return queryObjectList(null);
    }

    public List<Map<String, Object>> queryMapList() {
        return this.queryMapList(null, null);
    }

    public List<Map<String, Object>> queryMapList(Sort sort) {
        this.prepareListQuery(this.sql, sort);
        return this.mapList();
    }

    public List<Map<String, Object>> queryMapList(
            AnonymousResultTransformer anonymousResultTransformer) {
        return queryHumpMapList(null, anonymousResultTransformer);
    }

    public List<Map<String, Object>> queryMapList(Sort sort,
            AnonymousResultTransformer anonymousResultTransformer) {
        return queryHumpMapList(sort, anonymousResultTransformer);
    }

    public List<Map<String, Object>> queryHumpMapList(Sort sort,
            AnonymousResultTransformer anonymousResultTransformer) {
        this.prepareListQuery(this.sql, sort);
        if (anonymousResultTransformer != null) {
            this.mapTransformer = new SimpleTransformer(anonymousResultTransformer);
        }
        return this.humpMapList();
    }

    public List<Map<String, Object>> queryHumpMapList(Sort sort) {
        return queryHumpMapList(sort, null);
    }

    public List<Map<String, Object>> queryHumpMapList() {
        return queryHumpMapList(null, null);
    }

    public T queryOne() {
        this.prepareOneQuery(this.sql);
        return this.one();
    }

    public Object queryObject() {
        this.prepareOneQuery(this.sql);
        return this.object();
    }

    public Map<String, Object> queryMap(AnonymousResultTransformer anonymousResultTransformer) {
        this.prepareOneQuery(this.sql);
        if (anonymousResultTransformer != null) {
            this.mapTransformer = new SimpleTransformer(anonymousResultTransformer);
        }
        return this.humpMap();
    }

    public Map<String, Object> queryMap() {
        this.prepareOneQuery(this.sql);
        return this.map();
    }

    public Map<String, Object> queryHumpMap() {
        this.prepareOneQuery(this.sql);
        return this.humpMap();
    }

    public int execSql() {
        this.query = this.buildQuery(this.sql);
        return this.query.executeUpdate();
    }

    protected abstract String getCountSql(String querySql);

    /**
     * 添加排序
     *
     * @param sort 排序信息
     * @author AQIU 2018年4月12日 下午5:37:22
     */
    private void appendSort(Sort sort) {
        if (isSorted) {
            return;
        }
        isSorted = true;
        StringJoiner joiner = new StringJoiner(",", this.sql + " order by ", "");
        sort.forEach(order -> joiner.add(order.getProperty() + " " + order.getDirection().name()));
        this.sql = joiner.toString();
    }

    private void fullParams(Query query) {
        // 给条件赋上值
        if (this.isItemParams) {
            if (CollectionUtils.notEmpty(this.params)) {
                for (int i = 0; i < this.params.length; i++) {
                    query.setParameter(i + 1, this.params[i]);
                }
            }
        } else {
            if (CollectionUtils.notEmpty(this.parameters)) {
                for (Map.Entry<String, ?> entry : this.parameters.entrySet()) {
                    query.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void limitOne() {
        this.sql = this.sql.concat(" limit 1");
    }

    private void beforePagingQuery(Pageable pageable) {
        this.prepareListQuery(this.sql, pageable.getSort());
        if (StringUtils.isEmpty(this.countSql)) {
            this.countSql();
        }
        this.countQuery = this.buildQuery(this.countSql);
        this.pageable(pageable);
    }

    /**
     * 列表查询之前 添加排序
     *
     * @param sort 排序规则
     */
    private void beforeListQuery(Sort sort) {
        if (sort != null) {
            this.appendSort(sort);
        }
    }

    private Query buildQuery(String sql) {
        Query query = this.createQuery(sql);
        this.fullParams(query);
        return query;
    }

    private void prepareListQuery(String sql, Sort sort) {
        this.beforeListQuery(sort);
        this.query = this.buildQuery(sql);
    }

    private void prepareOneQuery(String sql) {
        this.limitOne();
        this.query = this.buildQuery(sql);
    }

    public abstract Query createQuery(String sql);

}
