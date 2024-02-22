package com.github.aqiu202.excel.style;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;

public interface ExcelStyleBuilder {

    Font buildFont(FontProperty property);

    CellStyle createCellStyle();

    CellStyle buildCellStyle(StyleProperty property);

    CreationHelper getCreationHelper();

}
