package com.github.aqiu202.excel.convert;

public interface ConverterFactory {

    void clear();

    void removeConverter(String name);

    void addConverter(String name, Converter<?, ?> converter);

    void addConverter(NamedConverter<?, ?> namedConverter);

    Converter<?, ?> findConverter(String name);

}
