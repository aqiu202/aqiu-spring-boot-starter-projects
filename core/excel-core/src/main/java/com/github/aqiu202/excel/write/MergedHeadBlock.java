package com.github.aqiu202.excel.write;

public class MergedHeadBlock extends HeadBlock implements MergedHeadItem {

    private int rowspan;
    private int colspan;
    private boolean repeatContent = false;

    public MergedHeadBlock(HeadItem item, int rowspan, int colspan) {
        this(item.getIndex(), item.getContent(), rowspan, colspan, false);
    }

    public MergedHeadBlock(HeadItem item, int rowspan, int colspan, boolean repeatContent) {
        this(item.getIndex(), item.getContent(), rowspan, colspan, repeatContent);
    }

    public MergedHeadBlock(int rowIndex, int colIndex, String content, int rowspan, int colspan) {
        this(rowIndex, colIndex, content, rowspan, colspan, false);
    }

    public MergedHeadBlock(int rowIndex, int colIndex, String content, int rowspan, int colspan, boolean repeatContent) {
        super(rowIndex, colIndex, content);
        this.rowspan = rowspan;
        this.colspan = colspan;
        this.repeatContent = repeatContent;
    }

    public MergedHeadBlock(CellIndex index, String content, int rowspan, int colspan) {
        this(index, content, rowspan, colspan, false);
    }

    public MergedHeadBlock(CellIndex index, String content, int rowspan, int colspan, boolean repeatContent) {
        super(index, content);
        this.rowspan = rowspan;
        this.colspan = colspan;
        this.repeatContent = repeatContent;
    }

    @Override
    public int getRowspan() {
        return rowspan;
    }

    public void setRowspan(int rowspan) {
        this.rowspan = rowspan;
    }

    @Override
    public int getColspan() {
        return colspan;
    }

    public void setColspan(int colspan) {
        this.colspan = colspan;
    }

    @Override
    public boolean isRepeatContent() {
        return repeatContent;
    }

    public void setRepeatContent(boolean repeatContent) {
        this.repeatContent = repeatContent;
    }
}
