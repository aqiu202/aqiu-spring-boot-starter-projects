package com.github.aqiu202.excel.format;

import com.github.aqiu202.excel.format.wrap.ResultWrapper;

public interface FormatterFacade {
    FormatterFinder getFormatterFinder();

    ResultWrapper<?> format(Object target, FormatterProvider formatterProvider);

    <T> T parse(String text, Class<T> targetType, FormatterProvider formatterProvider);

}
