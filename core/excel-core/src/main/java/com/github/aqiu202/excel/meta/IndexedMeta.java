package com.github.aqiu202.excel.meta;

public class IndexedMeta implements DataMeta {

    private final int index;
    private final DataMeta meta;

    public IndexedMeta(int index, DataMeta meta) {
        this.index = index;
        this.meta = meta;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String getKey() {
        return this.meta.getKey();
    }

    @Override
    public String getFormula() {
        return this.meta.getFormula();
    }

    @Override
    public boolean isImage() {
        return this.meta.isImage();
    }

    @Override
    public ValueDescriptor getValueDescriptor() {
        return this.meta.getValueDescriptor();
    }

    @Override
    public HeadDescriptor getHeadDescriptor() {
        return this.meta.getHeadDescriptor();
    }

    @Override
    public int getOrder() {
        return this.meta.getOrder();
    }

    public DataMeta getMeta() {
        return this.meta;
    }
}
