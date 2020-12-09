package com.github.aqiu202.starters.jpa.param;

import java.util.HashMap;
import java.util.Map;

public final class ParamMap extends HashMap<String, Object> {

    public ParamMap() {
        super();
    }

    public ParamMap(Map<String, Object> parent) {
        super();
        super.putAll(parent);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ParamMap set(String key, Object value) {
        super.put(key, value);
        return this;
    }

}
