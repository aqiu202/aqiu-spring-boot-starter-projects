package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.util.ClassUtils;
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

        if (ClassUtils.isAssignableFrom(Integer.class, type)) {
            return (T) Integer.valueOf(number.intValue());
        }
        if (ClassUtils.isAssignableFrom(Long.class, type)) {
            return (T) Long.valueOf(number.longValue());
        }
        if (ClassUtils.isAssignableFrom(Short.class, type)) {
            return (T) Short.valueOf(number.shortValue());
        }
        if (ClassUtils.isAssignableFrom(Byte.class, type)) {
            return (T) Byte.valueOf(number.byteValue());
        }
        if (ClassUtils.isAssignableFrom(Double.class, type)) {
            return (T) Double.valueOf(number.doubleValue());
        }
        if (ClassUtils.isAssignableFrom(Float.class, type)) {
            return (T) Float.valueOf(number.floatValue());
        }
        if (ClassUtils.isAssignableFrom(BigInteger.class, type)) {
            return (T) number.toBigInteger();
        }
        if (ClassUtils.isAssignableFrom(BigDecimal.class, type)) {
            return (T) number;
        }
        return null;
    }
}
