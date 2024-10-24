package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.meta.DataMeta;

import java.util.List;

public interface FormulaResolver {

    String getVariablePrefix();

    String getVariableSuffix();

    /**
     * 解析公式 （将带有占位符的表达式解析成真实的Excel公式）
     *
     * @param formula  公式
     * @param rowIndex 行索引
     * @param metaList 表头信息
     * @return 解析后的公式
     */
    String resolve(String formula, int rowIndex, List<? extends DataMeta> metaList);

}
