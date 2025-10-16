package com.github.aqiu202.http;

import com.github.aqiu202.http.util.JsonSerializer;
import com.github.aqiu202.http.util.RequestDescriptor;

import java.util.StringJoiner;

public class HttpBodyRequest extends HttpRequest<HttpBodyRequest> {

    public HttpBodyRequest(RequestDescriptor descriptor) {
        super(descriptor);
        this.jsonSerializer = descriptor.getJsonSerializer();
    }

    private Object body;

    private JsonSerializer jsonSerializer;

    public JsonSerializer getJsonSerializer() {
        return jsonSerializer;
    }

    public HttpBodyRequest jsonSerializer(JsonSerializer jsonSerializer) {
        this.jsonSerializer = jsonSerializer;
        return this;
    }

    public HttpBodyRequest jsonBody(Object body) {
        this.getHeaders().json();
        return this.body(this.jsonSerializer.serialize(body));
    }

    public HttpBodyRequest body(Object body) {
        this.body = body;
        return this;
    }

    public Object getBody() {
        return this.body;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(String.format("Method：%s", this.getMethod()));
        joiner.add(String.format("Url：%s", this.getUriString()));
        joiner.add(String.format("Headers：%s", this.getHeaders()));
        Object body = this.getBody();
        if (body != null) {
            joiner.add(String.format("Body：%s", body));
        }
        return joiner.toString();
    }
}
