package com.github.aqiu202.starters.jpa.dao.impl;


import com.github.aqiu202.starters.jpa.dao.BaseRepository;
import java.io.Serializable;
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

    /**
     * 父类没有不带参数的构造方法，这里手动构造父类
     *
     * @param entityClass entityClass
     * @param entityManager enyManager
     */
    public BaseRepositoryImpl(Class<T> entityClass,
            EntityManager entityManager) {
        super(new JpaMetamodelEntityInformation<>(entityClass, entityManager.getMetamodel()),
                entityManager);
    }

    @Override
    public boolean exists(Specification<T> specification) {
        return super.count(specification) > 0;
    }

}
