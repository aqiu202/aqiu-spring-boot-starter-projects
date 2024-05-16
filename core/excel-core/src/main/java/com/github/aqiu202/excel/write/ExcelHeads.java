package com.github.aqiu202.excel.write;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ExcelHeads implements Heads {

    private final Map<CellIndex, HeadItem> itemMap = new LinkedHashMap<>();
    private int rows;
    private int columns;

    public void addHead(HeadItem item) {
        CellIndex index = item.getIndex();
        this.itemMap.putIfAbsent(index, item);
        int rows = index.getRowIndex() + 1;
        int columns = index.getColIndex() + 1;
        if (item instanceof MergedHeadItem) {
            MergedHeadItem mergedHeadItem = (MergedHeadItem) item;
            rows = rows + mergedHeadItem.getRowspan() - 1;
            columns = columns + mergedHeadItem.getColspan() - 1;
        }
        this.rows = Math.max(this.rows, rows);
        this.columns = Math.max(this.columns, columns);
    }

    public void setHead(HeadItem item) {
        CellIndex index = item.getIndex();
        this.itemMap.put(index, item);
    }

    @Nullable
    @Override
    public HeadItem getHead(CellIndex index) {
        return this.itemMap.get(index);
    }

    @Override
    public List<HeadItem> getAllHeadItems() {
        return new ArrayList<>(this.itemMap.values());
    }

    @Override
    public int getHeadColumns() {
        return this.columns;
    }

    @Override
    public int getHeadRows() {
        return this.rows;
    }

    public static HeadsBuilder builder() {
        return new HeadsBuilder();
    }

    public static class HeadsBuilder {

        private final ExcelHeads heads = new ExcelHeads();

        private HeadsBuilder() {
        }

        public static HeadsBuilder builder() {
            return new HeadsBuilder();
        }

        public HeadsBuilder addHead(HeadItem item) {
            this.heads.addHead(item);
            return this;
        }

        public HeadsBuilder addHead(int rowIndex, int colIndex, String content) {
            return this.addHead(new HeadBlock(rowIndex, colIndex, content));
        }

        public HeadsBuilder addRowMergedHead(int rowIndex, int colIndex, String content, int colspan) {
            return this.addHead(new MergedHeadBlock(rowIndex, colIndex, content, 1, colspan));
        }

        public HeadsBuilder addColMergedHead(int rowIndex, int colIndex, String content, int rowspan) {
            return this.addHead(new MergedHeadBlock(rowIndex, colIndex, content, rowspan, 1));
        }

        public HeadsBuilder addHead(int rowIndex, int colIndex, String content, int rowspan, int colspan) {
            return this.addHead(new MergedHeadBlock(rowIndex, colIndex, content, rowspan, colspan));
        }

        public HeadsBuilder addHead(int rowIndex, int colIndex, String content, int rowspan, int colspan, boolean repeatContent) {
            return this.addHead(new MergedHeadBlock(rowIndex, colIndex, content, rowspan, colspan, repeatContent));
        }

        public HeadsBuilder setHead(HeadItem item) {
            this.heads.setHead(item);
            return this;
        }

        public HeadsBuilder setHead(int rowIndex, int colIndex, String content) {
            return this.setHead(new HeadBlock(rowIndex, colIndex, content));
        }


        public HeadsBuilder setHead(int rowIndex, int colIndex, String content, int rowspan, int colspan) {
            return this.setHead(new MergedHeadBlock(rowIndex, colIndex, content, rowspan, colspan));
        }

        public HeadsBuilder setHead(int rowIndex, int colIndex, String content, int rowspan, int colspan, boolean repeatContent) {
            return this.setHead(new MergedHeadBlock(rowIndex, colIndex, content, rowspan, colspan, repeatContent));
        }

        public HeadsBuilder replaceHead(int rowIndex, int colIndex, int rowspan, int colspan) {
            HeadItem head = this.heads.getHead(rowIndex, colIndex);
            if (head != null) {
                return this.setHead(new MergedHeadBlock(head, rowspan, colspan));
            }
            return this;
        }

        public HeadsBuilder replaceHead(int rowIndex, int colIndex, int rowspan, int colspan, boolean repeatContent) {
            HeadItem head = this.heads.getHead(rowIndex, colIndex);
            if (head != null) {
                return this.setHead(new MergedHeadBlock(head, rowspan, colspan, repeatContent));
            }
            return this;
        }

        public Heads build() {
            return this.heads;
        }

    }
}
