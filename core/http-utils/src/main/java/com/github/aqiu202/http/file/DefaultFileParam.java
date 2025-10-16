package com.github.aqiu202.http.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DefaultFileParam implements FileParam {
    private String filename;
    private final InputStream inputStream;
    private byte[] data;
    private final String contentType;

    public DefaultFileParam(InputStream inputStream, String contentType) {
        this.inputStream = inputStream;
        this.contentType = contentType;
    }

    public DefaultFileParam(byte[] data, String contentType) {
        this.inputStream = new ByteArrayInputStream(data);
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

    @Override
    public InputStream getInputStream() {
        return inputStream;
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public byte[] getBytes() throws IOException {
        if (this.data == null) {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            InputStream inputStream = this.getInputStream();
            int nRead;
            byte[] data = new byte[2048]; // 缓冲区大小，可以根据需要调整
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            this.data = buffer.toByteArray();
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
