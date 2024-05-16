package com.github.aqiu202.excel.read.cell;

import org.apache.poi.ss.usermodel.Cell;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberCellVal extends SimpleCellVal<BigDecimal> {
    public NumberCellVal(Cell cell, Double value) {
        super(cell, BigDecimal.valueOf(value));
    }
    public NumberCellVal(Cell cell, BigDecimal value) {
        super(cell, value);
    }

    public <T> T convertAs(Class<T> type) {
        BigDecimal number = this.getValue();

        if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
            return (T) Integer.valueOf(number.intValue());
        }
        if (type.equals(Long.class) || type.equals(Long.TYPE)) {
            return (T) Long.valueOf(number.longValue());
        }
        if (type.equals(Short.class) || type.equals(Short.TYPE)) {
            return (T) Short.valueOf(number.shortValue());
        }
        if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
            return (T) Byte.valueOf(number.byteValue());
        }
        if (type.equals(Double.class) || type.equals(Double.TYPE)) {
            return (T) Double.valueOf(number.doubleValue());
        }
        if (type.equals(Float.class) || type.equals(Float.TYPE)) {
            return (T) Float.valueOf(number.floatValue());
        }
        if (type.equals(BigInteger.class)) {
            return (T) number.toBigInteger();
        }
        if (type.equals(BigDecimal.class)) {
            return (T) number;
        }
        return null;
    }
}
