package com.github.aqiu202.starters.jpa.config;

import com.github.aqiu202.starters.jpa.anno.EnableBaseJpaRepositories;
import com.github.aqiu202.starters.jpa.query.dsl.QueryDslAutoConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class JpaDSLSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableBaseJpaRepositories.class.getName(), false);
        if (map == null) {
            return new String[0];
        }
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(map);
        List<String> list = new ArrayList<>();
        boolean enableExecutors = attributes.getBoolean("enableExecutors");
        boolean enableRetry = attributes.getBoolean("enableRetry");
        if (enableExecutors) {
            list.add(QueryDslAutoConfiguration.class.getName());
        }
        final boolean enableAuditing = attributes.getBoolean("enableAuditing");
        if (enableAuditing) {
            list.add(OpenJpaAuditingConfiguration.class.getName());
        }
        if (enableRetry) {
            list.add(RetryConfiguration.class.getName());
        }
        return list.toArray(new String[0]);
    }
}
