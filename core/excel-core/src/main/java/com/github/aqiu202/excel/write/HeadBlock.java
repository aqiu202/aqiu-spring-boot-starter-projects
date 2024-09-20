package com.github.aqiu202.excel.write;

public class HeadBlock implements HeadItem {

    private final CellIndex index;
    private String content;

    public HeadBlock(int rowIndex, int colIndex, String content) {
        this(new CellIndex(rowIndex, colIndex), content);
    }

    public HeadBlock(CellIndex index, String content) {
        this.index = index;
        this.content = content;
    }

    @Override
    public CellIndex getIndex() {
        return this.index;
    }

    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
