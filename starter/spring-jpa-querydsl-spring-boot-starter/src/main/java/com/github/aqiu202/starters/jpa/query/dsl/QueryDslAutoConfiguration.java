package com.github.aqiu202.starters.jpa.query.dsl;

import com.github.aqiu202.starters.jpa.sql.SQLExecutor;
import com.github.aqiu202.starters.jpa.sql.SimpleSQLExecutor;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class QueryDslAutoConfiguration {

    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "transactionManager";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    
    @Bean
    @ConditionalOnMissingBean
    public JPAQueryExecutor jpaQueryExecutor() {
        return new SimpleJPAQueryExecutor(this.entityManager);
    }

    @Bean
    @ConditionalOnMissingBean
    public SQLExecutor sqlExecutor() {
        return new SimpleSQLExecutor(this.entityManager);
    }

    @Primary
    @Bean(name = DEFAULT_TRANSACTION_MANAGER_NAME)
    @ConditionalOnMissingBean(name = DEFAULT_TRANSACTION_MANAGER_NAME)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(this.entityManagerFactory);
        tm.setDataSource(dataSource);
        return tm;
    }

}
