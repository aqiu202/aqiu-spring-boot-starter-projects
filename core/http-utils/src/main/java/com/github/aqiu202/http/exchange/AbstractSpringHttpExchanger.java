package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.io.AbstractResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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

    /**
     * 以原始字节为基础构建响应：始终保留原始文本，再按目标类型转换。
     * <p>
     * 空 body 或非 JSON 文本不会抛出异常，转换失败时 body 为 {@code null}，
     * 失败原因记录在 {@link HttpResponseEntity#getConversionError()}。
     */
    @SuppressWarnings("unchecked")
    protected <S> HttpResponseEntity<S> convertResponse(HttpRequest<?> request, ResponseEntity<byte[]> response,
                                                        TypeSpec<S> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        org.springframework.http.HttpHeaders headers = response.getHeaders();
        headers.forEach((key, values) ->
                httpHeaders.add(key, values.toArray(new Object[0]))
        );
        int statusCode = response.getStatusCode().value();
        byte[] bytes = response.getBody();
        String rawBody = bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
        Type type = responseType.getType();
        S body = null;
        RuntimeException conversionError = null;
        if (type == byte[].class) {
            body = (S) bytes;
        } else if (type == String.class) {
            body = (S) rawBody;
        } else if (rawBody != null && !rawBody.isBlank()) {
            try {
                body = request.getJsonSerializer().deserialize(rawBody, type);
            } catch (RuntimeException e) {
                conversionError = e;
            }
        }
        return new HttpResponseEntity<>(statusCode, body, httpHeaders, rawBody, conversionError);
    }

    protected void checkFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("上传的文件名称不能为空");
        }
    }
}
