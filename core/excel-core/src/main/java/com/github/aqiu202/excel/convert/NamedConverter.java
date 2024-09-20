package com.github.aqiu202.excel.convert;

public interface NamedConverter<S, T> extends Converter<S, T> {

    String getName();
}
