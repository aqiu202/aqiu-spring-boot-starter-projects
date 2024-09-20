package com.github.aqiu202.excel.convert;

public interface DataConverter<S, T> {

    T convertValue(S target, ConverterProvider converterProvider, ConverterFactory converterFactory);


}
