package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.model.DataExtractMode;

public interface SwitchableExcelWriter<T extends CombineExcelWriter<?>> {

    T extractMode(DataExtractMode extractMode);

}
