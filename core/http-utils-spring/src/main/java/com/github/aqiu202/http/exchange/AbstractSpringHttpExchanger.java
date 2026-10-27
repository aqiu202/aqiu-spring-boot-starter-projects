package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpBodyRequest;
import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpBodyEntity;
import com.github.aqiu202.http.data.HttpHeaders;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.file.FileParam;
import com.github.aqiu202.http.file.SpringFileParams;
import com.github.aqiu202.http.util.TypeSpec;
import org.springframework.core.io.AbstractResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractSpringHttpExchanger<T> extends AbstractHttpExchanger<T> {

    /**
     * 各实现共用的 JDK {@link HttpClient} 默认配置：设置连接超时，并自动跟随重定向（不允许 HTTPS 降级到 HTTP），
     * 保证有状态 / 无状态、不同底层客户端之间的重定向行为一致。
     */
    protected static HttpClient.Builder defaultHttpClientBuilder(Duration connectTimeout) {
        return HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .followRedirects(HttpClient.Redirect.NORMAL);
    }

    protected static JdkClientHttpRequestFactory defaultRequestFactory(Duration connectTimeout, Duration readTimeout) {
        return defaultRequestFactory(defaultHttpClientBuilder(connectTimeout).build(), readTimeout);
    }

    protected static JdkClientHttpRequestFactory defaultRequestFactory(HttpClient httpClient, Duration readTimeout) {
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        return requestFactory;
    }

    /**
     * 处理请求体：Content-Type 为 {@code application/x-www-form-urlencoded} 时，
     * {@link HttpBodyEntity} 按 URL 编码表单提交，其余情况走 {@link #processBody(Object)}。
     */
    protected Object processBody(HttpBodyRequest request) throws IOException {
        Object body = request.getBody();
        if (body instanceof HttpBodyEntity && isFormUrlEncoded(request.getHeaders())) {
            return this.buildFormBody((HttpBodyEntity) body);
        }
        return this.processBody(body);
    }

    protected MultiValueMap<String, String> buildFormBody(HttpBodyEntity bodyEntity) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        for (Map.Entry<String, List<Object>> entry : bodyEntity.entrySet()) {
            List<Object> values = entry.getValue();
            if (values == null) {
                continue;
            }
            for (Object value : values) {
                if (value instanceof FileParam || value instanceof Resource) {
                    throw new IllegalArgumentException("application/x-www-form-urlencoded 表单不支持文件参数，请改用 multipart/form-data："
                            + entry.getKey());
                }
                form.add(entry.getKey(), String.valueOf(value));
            }
        }
        return form;
    }

    private static boolean isFormUrlEncoded(HttpHeaders headers) {
        String contentType = headers.getFirst(org.springframework.http.HttpHeaders.CONTENT_TYPE);
        return contentType != null && contentType.trim().toLowerCase(Locale.ROOT)
                .startsWith(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }

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
                                this.checkFilename(fileParam.getFilename());
                                // 使用字节数组资源，保证重试时请求体可重复发送
                                MultipartBodyBuilder.PartBuilder partBuilder = bodyBuilder.part(key,
                                        SpringFileParams.toByteArrayResource(fileParam));
                                String contentType = fileParam.getContentType();
                                if (contentType != null && !contentType.trim().isEmpty()) {
                                    partBuilder.contentType(MediaType.parseMediaType(contentType));
                                }
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
     * Spring 响应仅负责类型适配，解码和转换统一由核心完成。
     */
    protected <S> HttpResponseEntity<S> convertResponse(HttpRequest<?> request, ResponseEntity<byte[]> response,
                                                        TypeSpec<S> responseType) {
        HttpHeaders httpHeaders = new HttpHeaders();
        org.springframework.http.HttpHeaders headers = response.getHeaders();
        headers.forEach((key, values) -> httpHeaders.add(key, values.toArray(new Object[0])));
        return this.convertResponse(request, response.getStatusCode().value(), httpHeaders,
                response.getBody(), responseType);
    }

    protected void checkFilename(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("上传的文件名称不能为空");
        }
    }
}
