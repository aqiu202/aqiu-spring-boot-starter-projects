package com.github.aqiu202.excel.meta;

import com.github.aqiu202.excel.read.cell.CellValue;
import org.apache.poi.ss.usermodel.Cell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IndexMappingMeta implements MappingMeta {

    private final String fieldName;
    private final int index;
    private final int startColumnNum;

    public IndexMappingMeta(int index, String fieldName) {
        this(index, fieldName, 0);
    }

    public IndexMappingMeta(int index, String fieldName, int startColumnNum) {
        this.fieldName = fieldName;
        this.index = index;
        this.startColumnNum = startColumnNum;
    }

    public static List<IndexMappingMeta> generate(int startColumnNum, String... fieldNames) {
        return generate(startColumnNum, Arrays.asList(fieldNames));
    }

    public static List<IndexMappingMeta> generate(String... fieldNames) {
        return generate(0, fieldNames);
    }

    public static List<IndexMappingMeta> generate(int startColumnNum, List<String> fieldNames) {
        List<IndexMappingMeta> list = new ArrayList<>();
        for (int i = 0; i < fieldNames.size(); i++) {
            list.add(new IndexMappingMeta(i, fieldNames.get(i), startColumnNum));
        }
        return list;
    }

    public static List<IndexMappingMeta> generate(List<String> fieldNames) {
        return generate(0, fieldNames);
    }

    @Override
    public String getFieldName() {
        return this.fieldName;
    }

    @Override
    public String getFieldTitle() {
        return null;
    }

    @Override
    public boolean match(CellValue<?> metaCellValue) {
        Cell cell = metaCellValue.getCell();
        int columnIndex = cell.getColumnIndex();
        return columnIndex - this.startColumnNum == this.index;
    }
}
