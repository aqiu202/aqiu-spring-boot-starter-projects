package com.github.aqiu202.excel.format;

import com.github.aqiu202.excel.format.wrap.ValueWrapper;

/**
 * 格式化数据的门面接口
 */
public interface FormatterFacade {

    FormatterFinder getFormatterFinder();

    ValueWrapper<?> format(Object target, FormatterProvider formatterProvider);

    <T> T parse(String text, Class<T> targetType, FormatterProvider formatterProvider);

}
