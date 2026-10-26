package com.github.aqiu202.http.util;

import java.lang.reflect.Type;

public interface JsonSerializer {

    String serialize(Object object);

    /**
     * 反序列化；空字符串返回 {@code null}，解析失败抛出运行时异常。
     *
     * @param json 原始 JSON 文本
     * @param type 目标类型，支持 {@link java.lang.reflect.Type} 与 Jackson 的 JavaType
     * @return 反序列化结果
     */
    <T> T deserialize(String json, Type type);

    default <T> T deserialize(String json, Class<T> type) {
        return this.deserialize(json, (Type) type);
    }
}
