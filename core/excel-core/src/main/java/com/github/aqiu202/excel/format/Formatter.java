package com.github.aqiu202.excel.format;

/**
 * 通用格式化
 */
public interface Formatter<T> {

    String format(T target, String pattern);

    <S> S parse(String text, String pattern, Class<S> targetType);

}
