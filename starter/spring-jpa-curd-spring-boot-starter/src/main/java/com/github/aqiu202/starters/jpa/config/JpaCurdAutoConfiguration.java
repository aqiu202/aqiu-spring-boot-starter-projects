package com.github.aqiu202.starters.jpa.config;

import com.github.aqiu202.starters.jpa.executor.JpaExecutor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceUnit;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@AutoConfiguration(after = JpaRepositoriesAutoConfiguration.class)
@ConditionalOnProperty(prefix = "spring.jpa.curd", name = "enabled", havingValue = "true", matchIfMissing = true)
public class JpaCurdAutoConfiguration implements InitializingBean {

    public static final String DEFAULT_TRANSACTION_MANAGER_NAME = "transactionManager";

    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory factory) {
        this.entityManagerFactory = factory;
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void afterPropertiesSet() {
        JpaExecutorFacade.setExecutor(new JpaExecutor(this.entityManagerFactory, this.entityManager));
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
