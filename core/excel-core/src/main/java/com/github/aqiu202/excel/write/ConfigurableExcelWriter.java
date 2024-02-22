package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;

public interface ConfigurableExcelWriter<T extends ConfigurableExcelWriter<?>> {

    SheetWriteConfiguration getConfiguration();

    T configuration(SheetWriteConfiguration configuration);

}
