package com.github.aqiu202.http.file;

import javax.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public interface FileParam {

    @Nullable
    String getFilename();

    @Nullable
    String getContentType();

    default boolean isEmpty() {
        return this.getSize() == 0;
    }

    long getSize();

    byte[] getBytes() throws IOException;

    InputStream getInputStream() throws IOException;

    static FileParam of(byte[] data, String contentType) {
        return new DefaultFileParam(data, contentType);
    }

    static FileParam of(byte[] data, String contentType, String filename) {
        DefaultFileParam fileParam = new DefaultFileParam(data, contentType);
        fileParam.setFilename(filename);
        return fileParam;
    }

    static FileParam of(InputStream is, String contentType) {
        return new DefaultFileParam(is, contentType);
    }

    static FileParam of(InputStream is, String contentType, String filename) {
        DefaultFileParam fileParam = new DefaultFileParam(is, contentType);
        fileParam.setFilename(filename);
        return fileParam;
    }
}
