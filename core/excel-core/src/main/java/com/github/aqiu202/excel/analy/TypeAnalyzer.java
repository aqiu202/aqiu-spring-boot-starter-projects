package com.github.aqiu202.excel.analy;

import java.util.List;

public interface TypeAnalyzer<T> {

    List<T> analysis(Class<?> type);
}
