package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public class SimpleConfigurableHandlerExcelWriter<T extends ConfigurableExcelWriter<?>> extends SimpleHandlerObtainer implements ConfigurableExcelWriter<T> {

    protected SheetWriteConfiguration configuration = new SheetWriteConfiguration();

    @Override
    public SheetWriteConfiguration getConfiguration() {
        return configuration;
    }

    @Override
    public T configuration(SheetWriteConfiguration configuration) {
        this.configuration = configuration;
        return (T) this;
    }

}
