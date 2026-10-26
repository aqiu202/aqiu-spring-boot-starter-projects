package com.github.aqiu202.http.util;

import com.github.aqiu202.http.data.HttpQueries;
import tools.jackson.core.type.TypeReference;
import com.github.aqiu202.util.JacksonUtils;
import jakarta.annotation.Nullable;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class URIBuilder {

    private String uri;
    private String baseUrl;
    private String path;
    private String schema;
    private String userInfo;
    private String host;
    private String query;
    private String baseQuery;
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

    /**
     * 设置基础地址：末尾的 {@code /} 会被去掉以便拼接路径，带有的查询串会被保留并追加在最终地址中。
     */
    public URIBuilder baseUrl(String baseUrl) {
        String baseQuery = null;
        if (Strings.isNotBlank(baseUrl)) {
            int index = baseUrl.indexOf("?");
            if (index > -1) {
                baseQuery = baseUrl.substring(index + 1);
                baseUrl = baseUrl.substring(0, index);
            }
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
            }
        }
        this.baseUrl = baseUrl;
        this.baseQuery = baseQuery;
        return this;
    }

    /**
     * 设置路径：缺少前导 {@code /} 时自动补全，末尾的 {@code /} 按原样保留；{@code ?} 之后的部分作为原始查询串。
     */
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
        }
        this.path = path;
        return this;
    }

    /**
     * 按分段追加路径：每个分段独立做 UTF-8 编码后再用 {@code /} 连接，
     * 因此分段内的 {@code /}、空格、中文等字符不会破坏路径结构。
     *
     * @param segments 未编码的路径分段
     */
    public URIBuilder pathSegments(String... segments) {
        if (segments != null) {
            for (String segment : segments) {
                this.addPathSegment(segment);
            }
        }
        return this;
    }

    public URIBuilder pathSegments(Collection<String> segments) {
        if (segments != null) {
            segments.forEach(this::addPathSegment);
        }
        return this;
    }

    private void addPathSegment(@Nullable String segment) {
        if (segment == null) {
            return;
        }
        String encoded = encodePathSegment(segment);
        if (Strings.isBlank(this.path)) {
            this.path = "/" + encoded;
        } else if (this.path.endsWith("/")) {
            this.path = this.path + encoded;
        } else {
            this.path = this.path + "/" + encoded;
        }
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
        Map<String, Object> map = JacksonSerializer.INSTANCE.toMap(bean);
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

    /**
     * 按取值移除查询参数：只移除与给定参数相同的取值，同名参数的其他取值会被保留。
     */
    public URIBuilder removeQueryParams(HttpQueries params) {
        if (params != null) {
            params.forEach((name, values) -> {
                List<String> current = this.queryParams.get(name);
                if (current != null) {
                    values.forEach(current::remove);
                    if (current.isEmpty()) {
                        this.queryParams.remove(name);
                    }
                }
            });
        }
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

    /**
     * 查询参数键 / 值统一做 UTF-8 编码（空格编码为 {@code +}）；
     * {@link #baseUrl(String)} 自带的查询串与 {@link #query(String)} 设置的原始查询串保持原样，不做二次编码。
     */
    public StringBuilder buildQuery() {
        StringBuilder builder = new StringBuilder();
        // 使用绝对地址时不拼接基础地址，也就不带基础地址的查询串
        if (Strings.isBlank(this.uri) && Strings.isNotBlank(this.baseQuery)) {
            builder.append(this.baseQuery);
        }
        if (!this.queryParams.isEmpty()) {
            this.queryParams.forEach((name, values) -> {
                if (values == null || values.isEmpty()) {
                    return;
                }
                String encodedName = encode(name);
                for (String value : values) {
                    if (builder.length() > 0) {
                        builder.append('&');
                    }
                    builder.append(encodedName).append('=').append(encode(value));
                }
            });
        }
        if (Strings.isNotBlank(this.query)) {
            if (builder.length() > 0) {
                builder.append('&');
            }
            builder.append(this.query);
        }
        if (builder.length() == 0) {
            return builder;
        }
        return new StringBuilder("?").append(builder);
    }

    private static String encodePathSegment(String segment) {
        return encode(segment).replace("+", "%20");
    }

    private static String encode(String value) {
        return URLEncoder.encode(value == null ? "" : value, StandardCharsets.UTF_8);
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
         * 占位符按出现顺序依次替换，取值会按路径分段做 UTF-8 编码
         *
         * @param text      字符串
         * @param parameter 替换参数
         * @return 替换后的字符串
         * @throws IllegalArgumentException 存在未提供取值（或取值为 {@code null}）的占位符时
         */
        public static String replaceVariables(String text, Object... parameter) {
            int[] index = {0};
            return replaceVariables(text, name -> {
                int i = index[0]++;
                return parameter != null && i < parameter.length ? parameter[i] : null;
            });
        }

        /**
         * 占位符按名称替换，取值会按路径分段做 UTF-8 编码
         *
         * @param text      字符串
         * @param parameter 替换参数
         * @return 替换后的字符串
         * @throws IllegalArgumentException 存在未提供取值（或取值为 {@code null}）的占位符时
         */
        public static String replaceVariables(String text, Map<?, ?> parameter) {
            return replaceVariables(text, name -> parameter == null ? null : parameter.get(name));
        }

        private static String replaceVariables(String text, Function<String, Object> resolver) {
            if (isEmpty(text)) {
                return text;
            }
            StringBuilder builder = new StringBuilder(text.length());
            int cursor = 0;
            int startIndex = text.indexOf(PLACEHOLDER_PREFIX);
            while (startIndex != -1) {
                int endIndex = text.indexOf(PLACEHOLDER_SUFFIX, startIndex + PLACEHOLDER_PREFIX.length());
                if (endIndex == -1) {
                    break;
                }
                String name = text.substring(startIndex + PLACEHOLDER_PREFIX.length(), endIndex);
                Object value = resolver.apply(name);
                if (value == null) {
                    throw new IllegalArgumentException(
                            String.format("URI 变量 {%s} 未提供取值：%s", name, text));
                }
                builder.append(text, cursor, startIndex).append(encodePathSegment(String.valueOf(value)));
                cursor = endIndex + PLACEHOLDER_SUFFIX.length();
                startIndex = text.indexOf(PLACEHOLDER_PREFIX, cursor);
            }
            builder.append(text, cursor, text.length());
            return builder.toString();
        }
    }

}
