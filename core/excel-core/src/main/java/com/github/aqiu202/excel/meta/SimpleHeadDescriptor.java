package com.github.aqiu202.excel.meta;

public class SimpleHeadDescriptor implements HeadDescriptor {

    private final String[] contents;

    public SimpleHeadDescriptor(String... contents) {
        this.contents = contents;
    }

    @Override
    public String[] getContents() {
        return this.contents;
    }
}
