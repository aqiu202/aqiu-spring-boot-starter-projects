package com.github.aqiu202.http.data;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

class ListMap {

    private final Map<String, List<String>> values = new LinkedHashMap<>();

    /**
     * 追加取值，{@code null} 值会被忽略。
     */
    public ListMap add(String key, Object... values) {
        if (values != null) {
            for (Object value : values) {
                if (value == null) {
                    continue;
                }
                this.values.computeIfAbsent(this.resolveKey(key), k -> new ArrayList<>())
                        .add(String.valueOf(value));
            }
        }
        return this;
    }

    /**
     * 逐个追加目标中的取值（不会覆盖已有取值，也不会与目标共享列表）。
     */
    public ListMap addAll(ListMap target) {
        if (target != null) {
            target.getValues().forEach((key, vs) -> {
                if (vs != null) {
                    this.add(key, vs.toArray());
                }
            });
        }
        return this;
    }

    /**
     * 替换取值，{@code null} 值会被忽略；没有有效取值时移除该键。
     */
    public ListMap set(String key, Object... values) {
        List<String> list = values == null ? new ArrayList<>() : Arrays.stream(values)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            this.values.remove(this.resolveKey(key));
        } else {
            this.values.put(this.resolveKey(key), list);
        }
        return this;
    }

    public ListMap set(Map<String, List<?>> values) {
        if (values != null) {
            this.values.clear();
            values.forEach((key, vs) -> {
                if (vs != null) {
                    this.set(key, vs.toArray());
                }
            });
        }
        return this;
    }

    public Map<String, List<String>> getValues() {
        return values;
    }

    public List<String> get(String key) {
        return values.get(this.resolveKey(key));
    }

    public String getFirst(String key) {
        List<String> values = this.values.get(this.resolveKey(key));
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    public ListMap remove(String key) {
        values.remove(this.resolveKey(key));
        return this;
    }

    public boolean contains(String key) {
        return values.containsKey(this.resolveKey(key));
    }

    public Set<String> getKeys() {
        return values.keySet();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public void forEach(BiConsumer<String, List<String>> consumer) {
        this.values.forEach(consumer);
    }

    public void clear() {
        this.values.clear();
    }

    protected String resolveKey(String key) {
        return key.toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return this.values.toString();
    }
}
