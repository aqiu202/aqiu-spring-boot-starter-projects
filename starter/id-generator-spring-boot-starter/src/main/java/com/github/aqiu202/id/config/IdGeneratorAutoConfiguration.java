package com.github.aqiu202.id.config;

import com.github.aqiu202.id.IdGenerator;
import com.github.aqiu202.id.IdGeneratorFactory;
import com.github.aqiu202.id.factory.DefaultIdGeneratorFactory;
import com.github.aqiu202.id.prop.IdProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <pre>Id Generator自动配置</pre>
 *
 * @author aqiu 2020/12/3 12:59
 **/
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(IdProperties.class)
public class IdGeneratorAutoConfiguration {

    public static final String ID_GENERATOR_BEAN_NAME = "idGenerator";

    public static final String ID_GENERATOR_FACTORY_BEAN_NAME = "idGeneratorFactory";

    @Bean(name = ID_GENERATOR_FACTORY_BEAN_NAME)
    @ConditionalOnMissingBean(name = ID_GENERATOR_FACTORY_BEAN_NAME)
    public IdGeneratorFactory idGeneratorFactory(IdProperties idProperties) {
        return new DefaultIdGeneratorFactory(idProperties);
    }

    @Bean(name = ID_GENERATOR_BEAN_NAME)
    @ConditionalOnMissingBean(name = ID_GENERATOR_BEAN_NAME)
    public IdGenerator idGenerator(
            @Qualifier(ID_GENERATOR_FACTORY_BEAN_NAME) IdGeneratorFactory idGeneratorFactory) {
        return idGeneratorFactory.getIdGenerator();
    }
}
