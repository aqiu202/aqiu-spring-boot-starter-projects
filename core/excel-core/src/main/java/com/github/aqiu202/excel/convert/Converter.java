package com.github.aqiu202.excel.convert;

public interface Converter<S, T> {

    /**
     * 导出时数据转换
     *
     * @param source 源数据
     * @return 转换后的数据
     */
    T convert(S source);

    /**
     * 导入时数据转换
     * @param target 转换后的数据
     * @return 源数据
     */
    S from(T target);

}
