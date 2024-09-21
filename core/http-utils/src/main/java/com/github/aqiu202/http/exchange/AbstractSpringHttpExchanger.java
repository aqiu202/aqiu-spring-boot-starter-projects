package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.file.FileParam;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class AbstractSpringHttpExchanger<T> extends AbstractHttpExchanger<T> {

    protected Object processBody(Object body) throws IOException {
        if (body instanceof FileParam) {
            body = ((FileParam) body).getBytes();
        } else if (body instanceof HttpBodyEntity) {
            HttpBodyEntity bodyEntity = (HttpBodyEntity) body;
            MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
            if (!bodyEntity.isEmpty()) {
                for (Map.Entry<String, List<Object>> entry : bodyEntity.entrySet()) {
                    String key = entry.getKey();
                    List<Object> values = entry.getValue();
                    if (values != null && !values.isEmpty()) {
                        for (Object value : values) {
                            if (value instanceof FileParam) {
                                FileParam fileParam = (FileParam) value;
                                bodyBuilder.part(key, fileParam.toInputStreamResource());
                            } else {
                                if (value instanceof AbstractResource) {
                                    this.checkFilename(((AbstractResource) value).getFilename());
                                }
                                bodyBuilder.part(key, value);
                            }
                        }
                    }
                }
            }
            body = bodyBuilder.build();
        }
        return body;
    }

    protected <S> HttpResponseEntity<S> convertResponse(ResponseEntity<S> response) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.getValues().putAll(response.getHeaders());
        return new HttpResponseEntity<>(response.getStatusCode().value(),
                response.getBody(), httpHeaders);
    }

    protected void checkFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("上传的文件名称不能为空");
        }
    }
}
