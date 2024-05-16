package com.github.aqiu202.excel.format.wrap;

public class StringValueWrapper implements ValueWrapper<String> {

    private final String result;

    public StringValueWrapper(String result) {
        this.result = result;
    }

    @Override
    public String getValue() {
        return this.result;
    }

}
