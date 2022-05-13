package com.github.aqiu202.autolog.config;

import com.github.aqiu202.autolog.anno.EnableAutoLog;
import com.github.aqiu202.autolog.aop.DebugLogFilter;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;

public class AutoLogConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableAutoLog.class.getName()));
        List<String> beanNames = new ArrayList<>();
        beanNames.add(AutoLogConfigurationBean.class.getName());
        if (attributes.getBoolean("enable")) {
            beanNames.add(attributes.getEnum("mode") == AdviceMode.ASPECTJ ? AspectjAutoLogConfiguration.class.getName() : ProxyAutoLogConfiguration.class.getName());
        }
        if (attributes.getBoolean("debug")) {
            beanNames.add(DebugLogFilter.class.getName());
        }
        return beanNames.toArray(new String[0]);
    }
}
