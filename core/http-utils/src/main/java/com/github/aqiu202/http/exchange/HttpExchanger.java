package com.github.aqiu202.http.exchange;

import com.github.aqiu202.http.HttpRequest;
import com.github.aqiu202.http.data.HttpResponseEntity;
import com.github.aqiu202.http.util.TypeSpec;

import java.io.IOException;

public interface HttpExchanger {

    <T> HttpResponseEntity<T> exchange(HttpRequest<?> request, TypeSpec<T> responseType) throws IOException;

}
