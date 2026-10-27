package com.github.aqiu202.http.file;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Spring 文件类型与框架中立 {@link FileParam} 的适配入口。
 */
public final class SpringFileParams {

    private SpringFileParams() {
    }

    public static FileParam of(InputStreamResource resource) throws IOException {
        DefaultFileParam fileParam = new DefaultFileParam(resource.getInputStream(), null);
        fileParam.setFilename(resource.getFilename());
        return fileParam;
    }

    public static FileParam of(ByteArrayResource resource) {
        DefaultFileParam fileParam = new DefaultFileParam(resource.getByteArray(), null);
        fileParam.setFilename(resource.getFilename());
        return fileParam;
    }

    public static FileParam of(MultipartFile file) throws IOException {
        DefaultFileParam fileParam = new DefaultFileParam(file.getBytes(), file.getContentType());
        fileParam.setFilename(file.getOriginalFilename());
        return fileParam;
    }

    public static ByteArrayResource toByteArrayResource(FileParam fileParam) throws IOException {
        return new ByteArrayResource(fileParam.getBytes()) {
            @Override
            public String getFilename() {
                return fileParam.getFilename();
            }
        };
    }

    public static InputStreamResource toInputStreamResource(FileParam fileParam) throws IOException {
        return new InputStreamResource(fileParam.getInputStream()) {
            @Override
            public long contentLength() {
                return fileParam.getSize();
            }

            @Override
            public String getFilename() {
                return fileParam.getFilename();
            }
        };
    }
}
