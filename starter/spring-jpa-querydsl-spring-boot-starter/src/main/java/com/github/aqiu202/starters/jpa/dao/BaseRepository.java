package com.github.aqiu202.starters.jpa.dao;


import java.io.Serializable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 自定义jpaRepository扩展方法
 *
 * @author AQIU
 * @version 创建时间：2018年4月12日 上午11:03:55
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends
        JpaBaseRepository<T, ID> {

    boolean exists(Specification<T> specification);
}
