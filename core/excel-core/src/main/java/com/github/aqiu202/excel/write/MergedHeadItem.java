package com.github.aqiu202.excel.write;

public interface MergedHeadItem extends HeadItem {

    int getRowspan();

    int getColspan();

    default boolean isRepeatContent() {
        return false;
    }
}
