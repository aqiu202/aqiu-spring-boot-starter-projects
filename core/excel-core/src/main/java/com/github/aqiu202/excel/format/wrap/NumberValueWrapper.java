package com.github.aqiu202.excel.format.wrap;

import java.math.BigDecimal;

public class NumberValueWrapper implements ValueWrapper<Number> {

    private final Number result;

    public NumberValueWrapper(Number result) {
        this.result = result;
    }

    public NumberValueWrapper(String result) {
        this.result = new BigDecimal(result);
    }

    @Override
    public Number getValue() {
        return this.result;
    }
}
