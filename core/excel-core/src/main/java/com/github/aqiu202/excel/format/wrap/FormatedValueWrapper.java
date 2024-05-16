package com.github.aqiu202.excel.format.wrap;

public class FormatedValueWrapper implements ValueWrapper<String> {

    private final ValueWrapper<?> original;
    private final String formattedResult;

    public FormatedValueWrapper(ValueWrapper<?> original, String formattedResult) {
        this.original = original;
        this.formattedResult = formattedResult;
    }

    public ValueWrapper<?> getOriginal() {
        return this.original;
    }

    @Override
    public String getValue() {
        return this.formattedResult;
    }
}
