package com.github.aqiu202.excel.format;

/**
 * 空值格式化
 */
public interface NullFormatter {

    String format();

    Object parse(Class<?> targetType);

}
