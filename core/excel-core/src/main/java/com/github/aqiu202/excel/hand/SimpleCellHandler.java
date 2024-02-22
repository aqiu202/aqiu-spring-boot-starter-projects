package com.github.aqiu202.excel.hand;

import com.github.aqiu202.excel.format.wrap.*;
import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import com.github.aqiu202.excel.style.ExcelStyleBuilder;
import com.github.aqiu202.excel.style.StyleProperty;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class SimpleCellHandler implements CellHandler {

    @Override
    public void handle(int row, int col, Cell cell, ResultWrapper<?> result, ExcelStyleBuilder styleBuilder,
                       SheetWriteConfiguration configuration) {
        // 表头
        if (row == 0) {
            // 设置样式
            this.fullCellStyle(cell, styleBuilder, configuration.getHeadStyle());
            Object title = result.getResult();
            // 表头默认为字符串类型
            cell.setCellValue(String.valueOf(title));
        } else {
            // 设置样式
            this.fullCellStyle(cell, styleBuilder, configuration.getContentStyle());
            // 根据数据类型填充数据
            // 与前端统一风格，数值型右对齐
            if (result instanceof NumberResultWrapper) {
                cell.getCellStyle().setAlignment(HorizontalAlignment.RIGHT);
                cell.setCellValue(((NumberResultWrapper) result).getResult().doubleValue());
            } else if (result instanceof StringResultWrapper) {
                cell.setCellValue(((StringResultWrapper) result).getResult());
            } else if (result instanceof FormatResultWrapper) {
                cell.setCellValue(((FormatResultWrapper) result).getResult());
            } else if (result instanceof DateResultWrapper) {
                DataFormat dataFormat = styleBuilder.getCreationHelper().createDataFormat();
                cell.getCellStyle().setDataFormat(dataFormat.getFormat(configuration.getDefaultDateFormat()));
                cell.setCellValue(((DateResultWrapper) result).getResult());
            }
        }
    }

    protected void fullCellStyle(Cell cell, ExcelStyleBuilder styleBuilder, StyleProperty property) {
        CellStyle cellStyle = styleBuilder.buildCellStyle(property);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
    }
}
