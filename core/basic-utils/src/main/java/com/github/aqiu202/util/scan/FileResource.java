package com.github.aqiu202.util.scan;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FileResource extends AbstractScanResource {

    private final File file;

    public FileResource(File file, String fileName) {
        super(fileName);
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(this.file.toPath());
    }
}
