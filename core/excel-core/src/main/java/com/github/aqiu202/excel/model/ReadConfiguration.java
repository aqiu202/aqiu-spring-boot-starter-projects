package com.github.aqiu202.excel.model;

public class ReadConfiguration extends DataConfiguration {

    /**
     * 是否读取公式
     */
    private boolean readFormula = false;

    /**
     * 是否读取空字符
     */
    private boolean readEmptyText = false;

    public boolean isReadFormula() {
        return readFormula;
    }

    public void setReadFormula(boolean readFormula) {
        this.readFormula = readFormula;
    }

    public boolean isReadEmptyText() {
        return readEmptyText;
    }

    public void setReadEmptyText(boolean readEmptyText) {
        this.readEmptyText = readEmptyText;
    }

}
