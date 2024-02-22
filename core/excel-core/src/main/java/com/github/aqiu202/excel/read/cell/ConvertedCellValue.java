package com.github.aqiu202.excel.read.cell;

public class ConvertedCellValue extends SimpleCellValue<CellValue<?>> {
    public ConvertedCellValue(CellValue<?> original, CellValue<?> value) {
        super(original.getCell(), value);
        this.original = original;
    }

    public final CellValue<?> original;

    public CellValue<?> getOriginal() {
        return original;
    }
}
