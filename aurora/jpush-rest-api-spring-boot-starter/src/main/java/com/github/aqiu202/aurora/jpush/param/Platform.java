package com.github.aqiu202.aurora.jpush.param;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Platform {

    Android(new String[]{"android"}), Ios(new String[]{"ios"}), All(new String[]{"android", "ios"});

    private String[] values;

    Platform(String[] values) {
        this.values = values;
    }

    @JsonValue
    public String[] getValues() {
        return this.values;
    }

}
