package com.github.aqiu202.http.file;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;

public class DefaultFileParam implements FileParam {
    private String filename;
    private InputStream inputStream;
    private byte[] data;
    private final String contentType;

    public DefaultFileParam(InputStream inputStream, String contentType) {
        this.inputStream = inputStream;
        this.contentType = contentType;
    }

    public DefaultFileParam(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    @Override
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 原始流只能读取一次，因此首次读取时会缓存为字节数组，之后每次都返回新的流，
     * 保证打印日志（读取大小）、请求重试等场景下内容不会被读空。
     */
    @Override
    public InputStream getInputStream() {
        try {
            return new ByteArrayInputStream(this.getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public long getSize() {
        try {
            return this.getBytes().length;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public synchronized byte[] getBytes() throws IOException {
        if (this.data == null) {
            try (InputStream is = this.inputStream) {
                this.data = is.readAllBytes();
            }
            this.inputStream = null;
        }
        return this.data;
    }

    @Override
    public String toString() {
        return "DefaultFileParam{" +
                "filename=" + this.resolveValue(this.filename) +
                ", length=" + this.getSize() +
                ", contentType=" + this.resolveValue(this.contentType) + "}";
    }

    private String resolveValue(String value) {
        if (value != null && !value.trim().isEmpty()) {
            value = String.format("'%s'", value);
        }
        return value;
    }
}
