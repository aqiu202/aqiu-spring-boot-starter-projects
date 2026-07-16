package com.github.aqiu202.http.util;

import com.github.aqiu202.http.data.HttpQueries;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.aqiu202.util.JacksonUtils;
import org.springframework.lang.Nullable;

import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class URIBuilder {

    private String uri;
    private String baseUrl;
    private String path;
    private String schema;
    private String userInfo;
    private String host;
    private String query;
    private Integer port;
    private final HttpQueries queryParams = new HttpQueries();
    private final Map<String, Object> uriVariables = new HashMap<>();
    private Object[] uriVariableObjects;

    public URIBuilder() {
    }

    public URIBuilder uri(URI uri) {
        return this.uri(uri.toString());
    }

    public URIBuilder uri(String uri) {
        this.uri = uri;
        return this;
    }

    public URIBuilder baseUrl(String baseUrl) {
        if (Strings.isNotBlank(baseUrl)) {
            int index = baseUrl.indexOf("?");
            if (index > -1) {
                baseUrl = baseUrl.substring(0, index);
            }
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
        }
        this.baseUrl = baseUrl;
        return this;
    }

    public URIBuilder path(String path) {
        if (Strings.isNotBlank(path)) {
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            int index = path.indexOf("?");
            if (index > -1) {
                // 设置query
                this.query(path.substring(index + 1));
                path = path.substring(0, index);
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        this.path = path;
        return this;
    }

    public URIBuilder schema(String schema) {
        this.schema = schema;
        return this;
    }

    public URIBuilder userInfo(String userInfo) {
        this.userInfo = userInfo;
        return this;
    }

    public URIBuilder host(String host) {
        this.host = host;
        return this;
    }

    public URIBuilder port(Integer port) {
        this.port = port;
        return this;
    }

    public URIBuilder query(String query) {
        this.query = query;
        return this;
    }

    public URIBuilder addQueryBean(Object bean) {
        Map<String, Object> map = JacksonUtils.convert(bean, new TypeReference<Map<String, Object>>() {
        });
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                continue;
            }
            if (value instanceof Collection<?>) {
                this.addQueryParam(key, (Collection<?>) value);
            } else {
                this.addQueryParam(key, value);
            }
        }
        return this;
    }

    public URIBuilder addQueryParam(String name, @Nullable Object... values) {
        if (values != null) {
            this.queryParams.add(name, values);
        }
        return this;
    }

    public URIBuilder addQueryParam(String name, @Nullable Collection<?> values) {
        return addQueryParam(name, values == null ? null : values.toArray());
    }

    public URIBuilder setQueryParam(String name, @Nullable Object... values) {
        if (values != null) {
            this.queryParams.set(name, values);
        }
        return this;
    }

    public URIBuilder setQueryParam(String name, @Nullable Collection<?> values) {
        return setQueryParam(name, values == null ? null : values.toArray());
    }

    public URIBuilder addQueryParams(HttpQueries params) {
        if (params != null) {
            params.forEach(this::addQueryParam);
        }
        return this;
    }

    public URIBuilder setQueryParams(HttpQueries queryParams) {
        this.queryParams.clear();
        this.queryParams.addAll(queryParams);
        return this;
    }

    public URIBuilder uriVariables(Map<String, ?> uriVariables) {
        if (uriVariables != null) {
            uriVariables.forEach(this::uriVariable);
        }
        return this;
    }

    public URIBuilder uriVariables(Object... uriVariables) {
        if (uriVariables != null && uriVariables.length > 0) {
            this.uriVariableObjects = uriVariables;
        }
        return this;
    }

    public URIBuilder uriVariable(String name, Object value) {
        this.uriVariables.put(name, value);
        return this;
    }

    protected StringBuilder buildTemplate() {
        StringBuilder builder = new StringBuilder();
        if (Strings.isNotBlank(this.uri)) {
            builder.append(this.uri);
        } else {
            if (Strings.isNotBlank(this.baseUrl)) {
                builder.append(this.baseUrl);
            } else {
                if (Strings.isNotBlank(this.schema)) {
                    builder.append(this.schema).append("://");
                }
                if (Strings.isNotBlank(this.userInfo)) {
                    builder.append(this.userInfo).append("@");
                }
                if (Strings.isNotBlank(this.host)) {
                    builder.append(this.host);
                }
                if (this.port != null) {
                    builder.append(":").append(this.port);
                }
            }
            if (Strings.isNotBlank(this.path)) {
                builder.append(this.path);
            }
        }
        StringBuilder queryBuilder = this.buildQuery();
        if (queryBuilder.length() > 0) {
            builder.append(queryBuilder);
        }
        return builder;
    }

    public StringBuilder buildQuery() {
        StringBuilder builder = new StringBuilder();
        if (!this.queryParams.isEmpty()) {
            builder.append("?");
            this.queryParams.forEach((k, v) -> {
                if (v != null) {
                    v.forEach(value -> builder.append(k).append("=").append(value).append("&"));
                }
            });
            builder.deleteCharAt(builder.length() - 1);
        }
        if (Strings.isNotBlank(this.query)) {
            String s = builder.length() == 0 ? "?" : "&";
            builder.append(s).append(this.query);
        }
        return builder;
    }

    public String buildQueryString() {
        return this.buildQuery().toString();
    }

    public URI buildWithVariables(Object... uriVariables) {
        StringBuilder buildTemplate = this.buildTemplate();
        return URI.create(Strings.replaceVariables(buildTemplate.toString(), uriVariables));
    }

    public URI build(Map<String, Object> uriVariables) {
        StringBuilder buildTemplate = this.buildTemplate();
        return URI.create(Strings.replaceVariables(buildTemplate.toString(), uriVariables));
    }

    public URI build() {
        return this.uriVariableObjects != null ?
                this.buildWithVariables(this.uriVariableObjects) :
                this.build(this.uriVariables);
    }

    public String toUrlString() {
        return this.build().toString();
    }

    public String toUrlStringWithVariables(Object... uriVariables) {
        return this.buildWithVariables(uriVariables).toString();
    }

    public String toUrlString(Map<String, Object> uriVariables) {
        return this.build(uriVariables).toString();
    }

    private static class Strings {
        /**
         * 占位符前缀: "{"
         */
        public static final String PLACEHOLDER_PREFIX = "{";
        /**
         * 占位符的后缀: "}"
         */
        public static final String PLACEHOLDER_SUFFIX = "}";

        public static boolean isEmpty(CharSequence str) {
            return str == null || str.length() == 0;
        }

        public static boolean hasText(CharSequence str) {
            return isNotEmpty(str) && containsText(str);
        }

        public static boolean isNotEmpty(CharSequence str) {
            return !isEmpty(str);
        }

        public static boolean isBlank(CharSequence str) {
            return isEmpty(str) || !containsText(str);
        }

        public static boolean isNotBlank(CharSequence str) {
            return !isBlank(str);
        }

        private static boolean containsText(CharSequence str) {
            int strLen = str.length();
            for (int i = 0; i < strLen; i++) {
                if (!Character.isWhitespace(str.charAt(i))) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 占位符替换
         *
         * @param text      字符串
         * @param parameter 替换参数
         * @return 替换后的字符串
         */
        public static String replaceVariables(String text, Object... parameter) {
            if (isEmpty(text) || parameter == null || parameter.length == 0) {
                return text;
            }
            StringBuilder builder = new StringBuilder(text);
            int index = 0;
            int startIndex = builder.indexOf(PLACEHOLDER_PREFIX);
            while (startIndex != -1) {
                int endIndex = builder
                        .indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
                if (endIndex != -1) {
                    int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                    Object objectVal;
                    if (index >= parameter.length || (objectVal = parameter[index++]) == null) {
                        startIndex = nextIndex + 1;
                        continue;
                    }
                    String stringVal = String.valueOf(objectVal);
                    if (stringVal != null) {
                        builder.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), stringVal);
                        nextIndex = startIndex + stringVal.length();
                    }
                    startIndex = builder.indexOf(PLACEHOLDER_PREFIX, nextIndex);
                } else {
                    startIndex = -1;
                }
            }
            return builder.toString();
        }

        /**
         * 占位符替换
         *
         * @param text      字符串
         * @param parameter 替换参数
         * @return 替换后的字符串
         */
        public static String replaceVariables(String text, Map<?, ?> parameter) {
            if (isEmpty(text) || parameter == null || parameter.isEmpty()) {
                return text;
            }
            StringBuilder builder = new StringBuilder(text);
            int startIndex = builder.indexOf(PLACEHOLDER_PREFIX);
            while (startIndex != -1) {
                int endIndex = builder
                        .indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
                if (endIndex != -1) {
                    String placeholder = builder
                            .substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                    int nextIndex = endIndex + PLACEHOLDER_SUFFIX.length();
                    Object objectVal = parameter.get(placeholder);
                    if (objectVal == null) {
                        startIndex = nextIndex + 1;
                        continue;
                    }
                    String stringVal = String.valueOf(objectVal);
                    builder.replace(startIndex, endIndex + PLACEHOLDER_SUFFIX.length(), stringVal);
                    nextIndex = startIndex + stringVal.length();
                    startIndex = builder.indexOf(PLACEHOLDER_PREFIX, nextIndex);
                } else {
                    startIndex = -1;
                }
            }
            return builder.toString();
        }
    }

}
