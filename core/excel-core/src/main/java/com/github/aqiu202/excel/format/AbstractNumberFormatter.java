package com.github.aqiu202.excel.format;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class AbstractNumberFormatter implements NumberFormatter {

    @Override
    public <S> S parse(String text, String pattern, Class<S> targetType) {
        BigDecimal number = new BigDecimal(text);
        if (targetType.equals(byte.class) || targetType.equals(Byte.class)) {
            return (S) Byte.valueOf(number.byteValue());
        }
        if (targetType.equals(short.class) || targetType.equals(Short.class)) {
            return (S) Short.valueOf(number.shortValue());
        }
        if (targetType.equals(int.class) || targetType.equals(Integer.class)) {
            return (S) Integer.valueOf(number.intValue());
        }
        if (targetType.equals(long.class) || targetType.equals(Long.class)) {
            return (S) Long.valueOf(number.longValue());
        }
        if (targetType.equals(float.class) || targetType.equals(Float.class)) {
            return (S) Float.valueOf(number.floatValue());
        }
        if (targetType.equals(double.class) || targetType.equals(Double.class)) {
            return (S) Double.valueOf(number.doubleValue());
        }
        if (targetType.equals(BigInteger.class)) {
            return (S) number.toBigInteger();
        }
        return (S) new BigDecimal(text);
    }
}
