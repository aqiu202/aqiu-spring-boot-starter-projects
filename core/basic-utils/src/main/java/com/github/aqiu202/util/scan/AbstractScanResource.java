package com.github.aqiu202.util.scan;

public abstract class AbstractScanResource implements ScanResource {

    protected final String fileName;

    public AbstractScanResource(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

}
