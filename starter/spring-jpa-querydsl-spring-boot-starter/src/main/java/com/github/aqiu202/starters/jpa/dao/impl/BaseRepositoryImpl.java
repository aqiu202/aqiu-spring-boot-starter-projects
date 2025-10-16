package com.github.aqiu202.starters.jpa.dao.impl;


import com.github.aqiu202.starters.jpa.dao.BaseRepository;
import com.github.aqiu202.starters.jpa.sql.AbstractQuery;
import com.github.aqiu202.starters.jpa.sql.HqlQuery;
import com.github.aqiu202.starters.jpa.sql.NativeSqlQuery;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.EntityManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;

/**
 * 自定义jpaRepository的扩展类.
 *
 * <pre>
 * 封装一些涉及到原生sql的增删改查分页等功能
 * </pre>
 *
 * @author AQIU
 * @version 创建时间：2018年4月12日 上午9:48:13
 */
public class BaseRepositoryImpl<T, ID extends Serializable> extends JpaBaseRepositoryImpl<T, ID>
        implements BaseRepository<T, ID> {

    private final EntityManager entityManager;
    private final Class<T> entityClass;

    /**
     * 父类没有不带参数的构造方法，这里手动构造父类
     *
     * @param entityClass   entityClass
     * @param entityManager enyManager
     */
    public BaseRepositoryImpl(Class<T> entityClass,
        EntityManager entityManager) {
        super(new JpaMetamodelEntityInformation<>(entityClass, entityManager.getMetamodel()),
            entityManager);
        this.entityClass = entityClass;
        this.entityManager = entityManager;
    }

    @Override
    public boolean exists(Specification<T> specification) {
        return super.count(specification) > 0;
    }

    @Override
    public T getByPrimaryKey(ID id) {
        return this.entityManager.find(entityClass, id);
    }

    @Override
    public AbstractQuery<T> of(String sql, Map<String, Object> parameters) {
        return NativeSqlQuery.of(this.getEntityManager()).as(this.entityClass).init(sql, parameters);
    }

    @Override
    public AbstractQuery<T> of(String sql, Object... parameters) {
        return NativeSqlQuery.of(this.getEntityManager()).as(this.entityClass).init(sql, parameters);
    }

    /**
     * @author aqiu
     * 不支持关联查询和子查询
     * @see BaseRepository#hql(java.lang.String, java.util.Map)
     */
    @Override
    public AbstractQuery<T> hql(String hql, Map<String, Object> parameters) {
        return HqlQuery.of(this.getEntityManager()).as(this.entityClass).init(hql, parameters);
    }

    /**
     * @author aqiu
     * 不支持关联查询和子查询
     * @see BaseRepository#hql(java.lang.String, java.util.Map)
     */
    @Override
    public AbstractQuery<T> hql(String hql, Object... parameters) {
        return HqlQuery.of(this.getEntityManager()).as(this.entityClass).init(hql, parameters);
    }

    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

}
