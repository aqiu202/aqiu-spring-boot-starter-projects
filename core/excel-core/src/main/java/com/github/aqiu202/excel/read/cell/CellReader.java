package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.model.ReadConfiguration;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * Excel单元格读取器
 */
public interface CellReader {

    default CellVal<?> readCell(Cell cell) {
        return this.readCell(cell, new ReadConfiguration());
    }

    CellVal<?> readCell(Cell cell, ReadConfiguration configuration);

    HeadMeta readHead(Sheet sheet, int colIndex, int headRows);

    default HeadMeta[] readHeads(Sheet sheet, int headColumns, int headRows) {
        return this.readHeads(sheet, 0, headColumns, headRows);
    }
    default HeadMeta[] readHeads(Sheet sheet, int startColIndex, int headColumns, int headRows) {
        HeadMeta[] headMetas = new HeadMeta[headColumns];
        int endColIndex = startColIndex + headColumns;
        for (int i = startColIndex; i < endColIndex; i++) {
            headMetas[i] = this.readHead(sheet, i, headRows);
        }
        return headMetas;
    }


}
