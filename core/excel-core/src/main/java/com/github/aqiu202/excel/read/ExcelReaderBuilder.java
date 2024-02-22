package com.github.aqiu202.excel.read;

import com.github.aqiu202.excel.model.SheetReadConfiguration;

public abstract class ExcelReaderBuilder {

    protected SheetReadConfiguration configuration = new SheetReadConfiguration();

    public ExcelReaderBuilder startRowNumber(int startRowNumber) {
        if (this.configuration != null) {
            this.configuration.setStartRowNumber(startRowNumber);
        }
        return this;
    }

    public ExcelReaderBuilder endRowNumber(int endRowNumber) {
        if (this.configuration != null) {
            this.configuration.setEndRowNumber(endRowNumber);
        }
        return this;
    }

    public ExcelReaderBuilder setStartColumnNumber(int startColumnNumber) {
        if (this.configuration != null) {
            this.configuration.setStartColumnNumber(startColumnNumber);
        }
        return this;
    }

    public ExcelReaderBuilder setEndColumnNumber(int endColumnNumber) {
        if (this.configuration != null) {
            this.configuration.setEndColumnNumber(endColumnNumber);
        }
        return this;
    }

    public ExcelReaderBuilder configuration(SheetReadConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }


    public abstract ExcelReader build();
}
