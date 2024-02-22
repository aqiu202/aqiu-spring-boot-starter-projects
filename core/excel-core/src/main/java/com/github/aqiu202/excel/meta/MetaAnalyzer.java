package com.github.aqiu202.excel.meta;

import java.util.List;

/**
 * 字段元信息分析器
 */
public interface MetaAnalyzer<T extends MappingMeta> {

    /**
     * 根据读取的目标类型获取映射元数据
     * @param type 目标类型
     * @return 映射元数据集合
     */
    List<T> analysisMetas(Class<?> type);
}
