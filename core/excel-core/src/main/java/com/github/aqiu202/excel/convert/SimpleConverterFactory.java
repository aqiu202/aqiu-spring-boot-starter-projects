package com.github.aqiu202.excel.convert;

import java.util.HashMap;
import java.util.Map;

public class SimpleConverterFactory implements ConverterFactory {

    public static final ConverterFactory INSTANCE = new SimpleConverterFactory();

    private final Map<String, Converter<?, ?>> converterMap = new HashMap<>();

    @Override
    public void clear() {
        this.converterMap.clear();
    }

    @Override
    public void removeConverter(String name) {
        this.converterMap.remove(name);
    }

    @Override
    public void addConverter(String name, Converter<?, ?> converter) {
        this.converterMap.put(name, converter);
    }

    @Override
    public void addConverter(NamedConverter<?, ?> namedConverter) {
        this.addConverter(namedConverter.getName(), namedConverter);
    }

    @Override
    public Converter<?, ?> findConverter(String name) {
        return this.converterMap.get(name);
    }

}
