package com.github.aqiu202.excel.format.wrap;

import java.math.BigDecimal;

public class SimpleNumberResultWrapper implements NumberResultWrapper {

    private final Number result;

    public SimpleNumberResultWrapper(Number result) {
        this.result = result;
    }

    public SimpleNumberResultWrapper(String result) {
        this.result = new BigDecimal(result);
    }

    @Override
    public Number getResult() {
        return this.result;
    }
}
