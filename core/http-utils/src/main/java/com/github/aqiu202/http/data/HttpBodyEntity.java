package com.github.aqiu202.http.data;

import com.github.aqiu202.http.file.FileParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpBodyEntity {

    private final Map<String, List<Object>> parameters = new LinkedHashMap<>();

    public Object getFirst(String name) {
        List<Object> values = this.get(name);
        return values == null ? null : values.get(0);
    }

    public List<Object> get(String name) {
        return this.parameters.get(name);
    }

    public HttpBodyEntity addParameters(String name, Object... values) {
        this.parameters.compute(name, (k, v) -> {
            if (v == null) {
                v = new ArrayList<>();
            }
            for (Object value : values) {
                if (value == null) {
                    continue;
                }
                this.checkValue(value);
                v.add(value);
            }
            return v;
        });
        return this;
    }

    protected void checkValue(Object value) {
        if (value instanceof FileParam) {
            FileParam fileParam = (FileParam) value;
            String filename = fileParam.getFilename();
            if (filename == null || filename.trim().isEmpty()) {
                throw new IllegalArgumentException("上传的文件名称不能为空");
            }
        }
    }

    public boolean isEmpty() {
        return this.parameters.isEmpty();
    }

    public Iterable<? extends Map.Entry<String, List<Object>>> entrySet() {
        return this.parameters.entrySet();
    }

    @Override
    public String toString() {
        return this.parameters.toString();
    }

}
