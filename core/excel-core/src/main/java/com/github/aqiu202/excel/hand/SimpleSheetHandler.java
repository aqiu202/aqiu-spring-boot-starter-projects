package com.github.aqiu202.excel.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import java.math.BigDecimal;
import java.util.List;

public class SimpleSheetHandler implements SheetHandler {

    public void handle(int index, Sheet sheet, int columnCount, List<?> data, SheetWriteConfiguration configuration) {

        if (configuration.isAutoSizeColumn()) {
            // 设置列宽自适应
            if (sheet instanceof SXSSFSheet) {
                ((SXSSFSheet) sheet).trackAllColumnsForAutoSizing();
            }
            // 设置列宽自适应比例
            for (int i = 0; i < columnCount; i++) {
                sheet.autoSizeColumn(i);
                int width = BigDecimal.valueOf(sheet.getColumnWidth(i) * configuration.getAutoWidthRatio()).intValue();
                if (width<2000){
                    width = 2000;
                }
                sheet.setColumnWidth(i, width);
            }
        }
    }
}
