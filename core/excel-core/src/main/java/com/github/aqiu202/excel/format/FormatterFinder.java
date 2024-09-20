package com.github.aqiu202.excel.format;

public interface FormatterFinder {

    <T> T findFormatter(Class<T> formatterType);
}
