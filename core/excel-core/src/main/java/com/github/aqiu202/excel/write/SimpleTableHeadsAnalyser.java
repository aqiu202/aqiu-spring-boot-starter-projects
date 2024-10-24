package com.github.aqiu202.excel.write;

import com.github.aqiu202.excel.meta.HeadDescriptor;
import com.github.aqiu202.excel.meta.DataMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimpleTableHeadsAnalyser implements TableHeadsAnalyser {

    @Override
    public Heads analyse(List<? extends DataMeta> metas) {
        int headRows = metas.stream().map(item -> item.getHeadDescriptor().getContents().length)
                .max(Comparator.comparing(arr -> arr)).orElse(1);
        int headColumns = metas.size();
        boolean[][] matrix = new boolean[headRows][headColumns];
        ExcelHeads.HeadsBuilder builder = ExcelHeads.builder();
        for (int i = 0; i < headColumns; i++) {
            DataMeta meta = metas.get(i);
            HeadDescriptor hd = meta.getHeadDescriptor();
            String[] paddedContents = padding(hd.getContents(), headRows);
            for (int j = 0; j < headRows; j++) {
                String paddedContent = paddedContents[j];
                if (paddedContent != null) {
                    matrix[j][i] = true;
                    builder.addHead(j, i, paddedContent);
                }
            }
        }
        List<Area> areas = new ArrayList<>();
        // 逐行检查合并单元格
        for (int i = 0; i < headRows; i++) {
            boolean[] row = matrix[i];
            for (int j = 0; j < headColumns; j++) {
                if (row[j]) {
                    int rowspan = 1;
                    int colspan = 1;
                    if (j + 1 >= headColumns) {
                        break;
                    }
                    for (int k = j + 1; k < headColumns; k++) {
                        if (row[k]) {
                            break;
                        }
                        colspan++;
                    }
                    if (colspan > 1) {
                        areas.add(new Area(i, j, rowspan, colspan));
                    }
                }
            }
        }
        for (Area area : areas) {
            builder.replaceHead(area.getRowIndex(), area.getColIndex(), area.getRowspan(), area.getColspan());
        }
        // TODO 暂时不考虑表头的多行合并
        return builder.build();
    }

    /**
     * 填充数组（不足长度的数组前面的元素填充为null）
     *
     * @param contents 原始数组
     * @param length   填充后的长度
     * @return 填充后的数组
     */
    private String[] padding(String[] contents, int length) {
        if (contents.length == length) {
            return contents;
        }
        String[] newContents = new String[length];
        System.arraycopy(contents, 0, newContents, length - contents.length, contents.length);
        return newContents;
    }

    static class Area {
        private int rowIndex;
        private int colIndex;
        private int rowspan;
        private int colspan;

        public Area(int rowIndex, int colIndex, int rowspan, int colspan) {
            this.rowIndex = rowIndex;
            this.colIndex = colIndex;
            this.rowspan = rowspan;
            this.colspan = colspan;
        }

        public int getRowIndex() {
            return rowIndex;
        }

        public void setRowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
        }

        public int getColIndex() {
            return colIndex;
        }

        public void setColIndex(int colIndex) {
            this.colIndex = colIndex;
        }

        public int getRowspan() {
            return rowspan;
        }

        public void setRowspan(int rowspan) {
            this.rowspan = rowspan;
        }

        public int getColspan() {
            return colspan;
        }

        public void setColspan(int colspan) {
            this.colspan = colspan;
        }
    }
}
