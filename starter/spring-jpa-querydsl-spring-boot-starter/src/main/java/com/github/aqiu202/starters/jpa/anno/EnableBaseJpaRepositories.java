package com.github.aqiu202.starters.jpa.anno;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.github.aqiu202.starters.jpa.config.JpaDSLSelector;
import com.github.aqiu202.starters.jpa.factory.BaseRepositoryFactoryBean;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;
import org.springframework.data.repository.config.DefaultRepositoryBaseClass;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;

@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableJpaRepositories
@Import({JpaDSLSelector.class})
public @interface EnableBaseJpaRepositories {

    @AliasFor(annotation = EnableJpaRepositories.class)
    String[] value() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    String[] basePackages() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    Class<?>[] basePackageClasses() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    Filter[] includeFilters() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    Filter[] excludeFilters() default {};

    @AliasFor(annotation = EnableJpaRepositories.class)
    String repositoryImplementationPostfix() default "Impl";

    @AliasFor(annotation = EnableJpaRepositories.class)
    String namedQueriesLocation() default "";

    @AliasFor(annotation = EnableJpaRepositories.class)
    Key queryLookupStrategy() default Key.CREATE_IF_NOT_FOUND;

    @AliasFor(annotation = EnableJpaRepositories.class)
    Class<?> repositoryFactoryBeanClass() default BaseRepositoryFactoryBean.class;

    @AliasFor(annotation = EnableJpaRepositories.class)
    Class<?> repositoryBaseClass() default DefaultRepositoryBaseClass.class;

    @AliasFor(annotation = EnableJpaRepositories.class)
    String entityManagerFactoryRef() default "entityManagerFactory";

    @AliasFor(annotation = EnableJpaRepositories.class)
    String transactionManagerRef() default "transactionManager";

    @AliasFor(annotation = EnableJpaRepositories.class)
    boolean considerNestedRepositories() default false;

    @AliasFor(annotation = EnableJpaRepositories.class)
    boolean enableDefaultTransactions() default true;

    @AliasFor(annotation = EnableJpaRepositories.class)
    BootstrapMode bootstrapMode() default BootstrapMode.DEFAULT;

    boolean enableExecutors() default true;

    boolean enableRetry() default true;

    boolean enableAuditing() default true;
}
