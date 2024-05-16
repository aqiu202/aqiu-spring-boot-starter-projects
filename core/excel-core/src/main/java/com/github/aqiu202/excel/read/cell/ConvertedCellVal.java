package com.github.aqiu202.excel.read.cell;

public class ConvertedCellVal extends SimpleCellVal<CellVal<?>> {

    public ConvertedCellVal(CellVal<?> original, CellVal<?> value) {
        super(original.getCell(), value);
        this.original = original;
    }

    public final CellVal<?> original;

    public CellVal<?> getOriginal() {
        return original;
    }
}
