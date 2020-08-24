package com.github.aqiu202.autolog.config;

import com.github.aqiu202.autolog.anno.EnableAutoLog;
import com.github.aqiu202.autolog.aop.DebugLogFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class AutoLogConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> map = importingClassMetadata.getAnnotationAttributes(
                EnableAutoLog.class.getName(), false);
        if (map == null) {
            return new String[0];
        }
        AnnotationAttributes attributes = AnnotationAttributes
                .fromMap(map);
        List<String> beanNames = new ArrayList<>();
        if (attributes.getBoolean("enable")) {
            beanNames.add(AutoLogConfiguration.class.getName());
        }
        if (attributes.getBoolean("debug")) {
            beanNames.add(DebugLogFilter.class.getName());
        }
        return beanNames.toArray(new String[0]);
    }
}
