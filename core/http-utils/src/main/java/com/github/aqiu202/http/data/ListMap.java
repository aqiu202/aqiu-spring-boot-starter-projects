package com.github.aqiu202.http.data;

import java.util.*;
import java.util.function.BiConsumer;

class ListMap {

    private final Map<String, List<String>> values = new LinkedHashMap<>();

    public ListMap add(String key, Object... values) {
        if (values != null) {
            for (Object value : values) {
                this.values.compute(this.resolveKey(key), (k, v) -> {
                    if (v == null) {
                        v = new ArrayList<>();
                    }
                    v.add(String.valueOf(value));
                    return v;
                });
            }
        }
        return this;
    }

    public ListMap addAll(ListMap target) {
        this.values.putAll(target.getValues());
        return this;
    }

    public ListMap set(String key, Object... values) {
        this.values.put(this.resolveKey(key),
                Arrays.stream(values).map(String::valueOf).toList());
        return this;
    }

    public ListMap set(Map<String, List<?>> values) {
        if (values != null) {
            this.values.clear();
            values.forEach((key, vs) -> {
                if (vs != null) {
                    this.values.put(this.resolveKey(key), vs.stream().map(String::valueOf).toList());
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
