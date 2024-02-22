package com.github.aqiu202.excel.convert;

public interface Converter<S, T> {

    T convert(S source);
}
