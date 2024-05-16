package com.github.aqiu202.excel.format.wrap;

public class ImageValueWrapper implements ValueWrapper<String> {

    private final String result;

    public ImageValueWrapper(String result) {
        this.result = result;
    }

    @Override
    public String getValue() {
        return this.result;
    }
}
