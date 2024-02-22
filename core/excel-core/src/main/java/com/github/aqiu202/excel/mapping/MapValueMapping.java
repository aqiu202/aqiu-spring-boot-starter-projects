package com.github.aqiu202.excel.mapping;

import com.github.aqiu202.excel.meta.FieldProperty;
import com.github.aqiu202.excel.read.cell.CellValue;

import java.util.Collections;
import java.util.List;

public class MapValueMapping extends AbstractValueMapping<FieldProperty> {

    @Override
    public List<FieldProperty> analysisMetas(Class<?> type) {
        return Collections.emptyList();
    }

    @Override
    public FieldProperty select(CellValue<?> cv, List<FieldProperty> fields) {
        // 目标类型为Map时,表头的值作为key
        return new FieldProperty(cv.toString());
    }

}
