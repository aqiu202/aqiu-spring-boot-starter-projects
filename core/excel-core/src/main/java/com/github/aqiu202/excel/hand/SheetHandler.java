package com.github.aqiu202.excel.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

public interface SheetHandler {

    void handle(int index, Sheet sheet, int columnCount, List<?> value, SheetWriteConfiguration configuration);
}
