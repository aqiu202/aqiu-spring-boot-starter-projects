package com.github.aqiu202.excel.format.wrap;

public class SimpleStringResultWrapper implements StringResultWrapper {

    private final String result;

    public SimpleStringResultWrapper(String result) {
        this.result = result;
    }

    @Override
    public String getResult() {
        return this.result;
    }

}
