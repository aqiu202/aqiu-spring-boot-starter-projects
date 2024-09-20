package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.read.cell.RowMappedCellValues;

/**
 * Excel数据读取时将每行数据转换为特定的类型
 */
public interface TypeTranslator {

    /**
     * 将Excel数据转换为特定类型
     * @param rowMappedCellValues Excel数据
     * @param type 目标类型
     * @return 目标类型示例
     */
    <T> T translate(RowMappedCellValues rowMappedCellValues, Class<T> type);

}
