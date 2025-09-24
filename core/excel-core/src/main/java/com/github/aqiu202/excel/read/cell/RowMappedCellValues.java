package com.github.aqiu202.excel.read.cell;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public interface RowMappedCellValues {

    int getRowNum();

    MappedCellValue[] getMappedCellValues();

    default MappedCellValue getMappedCellValue(int index) {
        return this.getMappedCellValues()[index];
    }

    default void setMappedCellValue(int index, MappedCellValue mappedCellValue) {
        this.getMappedCellValues()[index] = mappedCellValue;
    }

    default Map<String, Object> toMap() {
        return Arrays.stream(this.getMappedCellValues())
                .collect(
                        Collectors.toMap(
                                v -> v.getTableMeta().getKey(),
                                v -> v.getCellValue().getValue(),
                                (v1, v2) -> v1
                        )
                );
    }

    default Object[] rawValues() {
        return Arrays.stream(this.getMappedCellValues())
                .map(v -> v.getCellValue().getValue())
                .toArray();
    }

}
