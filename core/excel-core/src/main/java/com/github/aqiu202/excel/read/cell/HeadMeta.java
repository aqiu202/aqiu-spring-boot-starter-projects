package com.github.aqiu202.excel.read.cell;

import com.github.aqiu202.excel.meta.TableMeta;
import com.github.aqiu202.util.CollectionUtils;
import com.github.aqiu202.util.StringUtils;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;
import java.util.StringJoiner;

/**
 * Excel表头读取时的元数据
 */
public class HeadMeta {

    private final CellVal<?>[] cells;

    private final int colIndex;

    public HeadMeta(int colIndex, List<CellVal<?>> cells) {
        this(colIndex, cells.toArray(new CellVal[0]));
    }

    public HeadMeta(int colIndex, CellVal<?>... cells) {
        this.colIndex = colIndex;
        this.cells = cells;
    }

    public CellVal<?>[] getCells() {
        return this.cells;
    }

    public CellVal<?> getCell(int index) {
        return this.cells[index];
    }

    public int getColIndex() {
        return colIndex;
    }

    public boolean isMatched(TableMeta vm) {
        String[] titles = vm.getHeadDescriptor().getContents();
        CellVal<?>[] cells = this.getCells();
        int index = 0;
        for (CellVal<?> cellVal : cells) {
            if (cellVal.getCell() == null || cellVal instanceof BlankCellVal) {
                continue;
            }
            String scv = cellVal.getCell().getStringCellValue();
            if (!StringUtils.equals(titles[index++], scv)) {
                return false;
            }
        }
        return true;
    }

    public String asText() {
        CellVal<?>[] cells = this.getCells();
        if (CollectionUtils.isEmpty(cells)) {
            return "";
        }
        StringJoiner joiner = new StringJoiner(".");
        for (CellVal<?> cellVal : cells) {
            Cell cell = cellVal.getCell();
            if (cell == null || cellVal instanceof BlankCellVal) {
                continue;
            }
            joiner.add(cell.getStringCellValue());
        }
        return joiner.toString();
    }
}
