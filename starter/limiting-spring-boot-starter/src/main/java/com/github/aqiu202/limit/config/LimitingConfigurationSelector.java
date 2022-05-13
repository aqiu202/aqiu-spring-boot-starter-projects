package com.github.aqiu202.limit.config;

import com.github.aqiu202.limit.anno.EnableLimiting;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;

public class LimitingConfigurationSelector extends AdviceModeImportSelector<EnableLimiting> {

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        switch (adviceMode) {
            case PROXY:
                return new String[]{ProxyLimitingConfiguration.class.getName()};
            case ASPECTJ:
                return new String[]{AspectjLimitingConfiguration.class.getName()};
            default:
                return null;
        }
    }
}
