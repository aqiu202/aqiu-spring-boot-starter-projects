package com.github.aqiu202.http.file;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

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

    default ByteArrayResource toByteArrayResource() throws IOException {
        return new ByteArrayResource(this.getBytes()) {
            @Override
            public String getFilename() {
                return FileParam.this.getFilename();
            }
        };
    }

    default InputStreamResource toInputStreamResource() throws IOException {
        return new InputStreamResource(getInputStream()) {
            @Override
            public long contentLength() {
                return FileParam.this.getSize();
            }

            @Override
            public String getFilename() {
                return FileParam.this.getFilename();
            }
        };

    }

    static FileParam of(InputStreamResource isr) throws IOException {
        return new DefaultFileParam(isr.getInputStream(), null);
    }

    static FileParam of(ByteArrayResource bar) {
        return new DefaultFileParam(bar.getByteArray(), null);
    }

    static FileParam of(MultipartFile file) throws IOException {
        DefaultFileParam fileParam = new DefaultFileParam(file.getInputStream(), file.getOriginalFilename());
        fileParam.setFilename(file.getOriginalFilename());
        return fileParam;
    }

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
