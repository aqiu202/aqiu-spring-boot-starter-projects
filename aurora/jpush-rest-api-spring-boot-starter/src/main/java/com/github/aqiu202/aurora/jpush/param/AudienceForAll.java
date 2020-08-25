package com.github.aqiu202.aurora.jpush.param;

import com.fasterxml.jackson.annotation.JsonValue;

public class AudienceForAll extends Audience {

    public static final Audience DEFAULT = new AudienceForAll();

    private static final String VALUE_ALL = "all";

    private AudienceForAll() {
    }

    @JsonValue
    public String getValue() {
        return VALUE_ALL;
    }

}
