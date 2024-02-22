package com.github.aqiu202.excel.convert;

public interface ConverterFactoryWrapper<T extends ConverterFactoryWrapper<?>> {

    T converterFactory(ConverterFactory converterFactory);

    ConverterFactory getConverterFactory();
}
