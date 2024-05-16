package com.github.aqiu202.excel.write.hand;

import com.github.aqiu202.excel.model.SheetWriteConfiguration;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Collection;

@FunctionalInterface
public interface SheetHandler {

    void handle(Sheet sheet, int columnCount, Collection<?> data, SheetWriteConfiguration configuration);
}
