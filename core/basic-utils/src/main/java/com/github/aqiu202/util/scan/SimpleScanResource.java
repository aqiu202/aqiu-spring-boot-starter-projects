package com.github.aqiu202.util.scan;

import java.io.InputStream;

public class SimpleScanResource implements ScanResource {

    private final String fileName;

    private final InputStream inputStream;

    public SimpleScanResource(String fileName, InputStream inputStream) {
        this.fileName = fileName;
        this.inputStream = inputStream;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public InputStream getInputStream() {
        return inputStream;
    }

}
