package com.github.aqiu202.aurora.jpush.util;

import java.io.IOException;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtil {

    public static CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    public static CloseableHttpResponse get(String url, List<Header> headers)
            throws IOException {
        CloseableHttpClient client = createHttpClient();
        HttpGet req = new HttpGet(url);
        config(req);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(req::addHeader);
        }
        return client.execute(req);
    }

    public static CloseableHttpResponse get(String url, List<Header> headers, int timeout)
            throws IOException {
        CloseableHttpClient client = createHttpClient();
        HttpGet req = new HttpGet(url);
        config(req, timeout);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(req::addHeader);
        }
        return client.execute(req);
    }

    public static CloseableHttpResponse post(String url, List<Header> headers, HttpEntity entity)
            throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost req = new HttpPost(url);
        config(req);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(req::addHeader);
        }
        if (entity != null) {
            req.setEntity(entity);
        }
        return client.execute(req);
    }

    public static CloseableHttpResponse post(String url, List<Header> headers, HttpEntity entity,
            int timeout)
            throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost req = new HttpPost(url);
        config(req, timeout);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach(req::addHeader);
        }
        if (entity != null) {
            req.setEntity(entity);
        }
        return client.execute(req);
    }

    public static CloseableHttpResponse post(String url, List<Header> headers)
            throws IOException {
        return post(url, headers, null);
    }

    public static CloseableHttpResponse postJson(String url, List<Header> headers, String json)
            throws IOException {
        return post(url, headers, new StringEntity(json, ContentType.APPLICATION_JSON));
    }

    public static CloseableHttpResponse post(String url, List<Header> headers, int timeout)
            throws IOException {
        return post(url, headers, null, timeout);
    }

    public static CloseableHttpResponse postJson(String url, List<Header> headers, String json,
            int timeout)
            throws IOException {
        return post(url, headers, new StringEntity(json, ContentType.APPLICATION_JSON), timeout);
    }

    private static void config(HttpRequestBase request) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(5000)
                .setSocketTimeout(5000)
                .setConnectTimeout(5000).build();// 设置请求和传输超时时间
        request.setConfig(requestConfig);
    }

    private static void config(HttpRequestBase request, int timeout) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).setConnectTimeout(timeout).build();// 设置请求和传输超时时间
        request.setConfig(requestConfig);
    }
}
