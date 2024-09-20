package com.github.aqiu202.excel.write;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public interface Exportable {

    void exportTo(OutputStream os);

    default void exportTo(File file) {
        try (FileOutputStream os = new FileOutputStream(file)) {
            this.exportTo(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void exportTo(String filePath) {
        this.exportTo(new File(filePath));
    }
}
