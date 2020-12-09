package com.github.aqiu202.starters.jpa.config;

import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * <pre>开启JpaAuditing</pre>
 *
 * @author aqiu 2020/11/17 8:22
 **/
@EnableJpaAuditing
public class OpenJpaAuditingConfiguration {

//    @Bean
//    @ConditionalOnMissingBean
//    public AuditorAware<String> stringAuditorAware() {
//        return () -> "XXX";
//    }
}
