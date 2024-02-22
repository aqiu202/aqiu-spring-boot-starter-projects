package com.github.aqiu202.excel.format.wrap;

public class SimpleFormatResultWrapper implements FormatResultWrapper {

    private final ResultWrapper<?> original;
    private final String formattedResult;

    public SimpleFormatResultWrapper(ResultWrapper<?> original, String formattedResult) {
        this.original = original;
        this.formattedResult = formattedResult;
    }

    @Override
    public ResultWrapper<?> getOriginal() {
        return this.original;
    }

    @Override
    public String getResult() {
        return this.formattedResult;
    }
}
