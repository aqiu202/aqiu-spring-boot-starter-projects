package com.github.aqiu202.excel.convert;

import java.util.Map;

public interface MapConverter<S, T> extends Converter<S, T>{

    Map<S, T> getMap();
}
