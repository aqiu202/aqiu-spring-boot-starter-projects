package com.github.aqiu202.aurora.jpush.param;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Notification {

    private String alert;

    private Map<String, Object> android;

    private Map<String, Object> ios;

    private Notification(Platform platform) {
        switch (platform) {
            case All:
                this.android = new HashMap<>();
                this.ios = new HashMap<>();
                this.ios.put("sound", "default");
                this.ios.put("badge", 1);
                break;
            case Android:
                this.android = new HashMap<>();
                break;
            case Ios:
                this.ios = new HashMap<>();
                this.ios.put("sound", "default");
                this.ios.put("badge", 1);
                break;
            default:
                break;
        }
    }

    public static Notification of(Platform platform) {
        return new Notification(platform);
    }

    public void setStyle(Style style) {
        if (Objects.nonNull(this.android)) {
            this.android.put("style", style.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public void setTitle(String title) {
        if (Objects.nonNull(this.android)) {
            this.android.put("title", title);
        }
        if (Objects.nonNull(this.ios)) {
            Map<String, Object> alert;
            if (this.ios.get("alert") == null) {
                alert = new HashMap<>();
            } else {
                alert = (Map<String, Object>) this.ios.get("alert");
            }
            alert.put("title", title);
            this.ios.put("alert", alert);
        }
    }

    @SuppressWarnings("unchecked")
    public void setContent(String content) {
        this.alert = content;
        Map<String, Object> alert;
        if (Objects.nonNull(this.ios) && (Objects
                .nonNull(alert = (Map<String, Object>) this.ios.get("alert")))) {
            alert.put("body", content);
        }
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public Map<String, Object> getAndroid() {
        return android;
    }

    public void setAndroid(Map<String, Object> android) {
        this.android = android;
    }

    public Map<String, Object> getIos() {
        return ios;
    }

    public void setIos(Map<String, Object> ios) {
        this.ios = ios;
    }

    @SuppressWarnings("unchecked")
    public void addExtras(Map<String, Object> value) {
        if (Objects.nonNull(this.android)) {
            Map<String, Object> extras = null;
            if (this.android.get("extras") != null) {
                extras = (Map<String, Object>) this.android.get("extras");
            } else {
                extras = new HashMap<>();
            }
            extras.putAll(value);
            this.android.put("extras", extras);
        }
        if (Objects.nonNull(this.ios)) {
            Map<String, Object> extras = null;
            if (this.ios.get("extras") != null) {
                extras = (Map<String, Object>) this.ios.get("extras");
            } else {
                extras = new HashMap<>();
            }
            extras.putAll(value);
            this.ios.put("extras", extras);
        }
    }

    @SuppressWarnings("unchecked")
    public void addExtra(String key, Object value) {
        if (Objects.nonNull(this.android)) {
            Map<String, Object> extras = null;
            if (this.android.get("extras") != null) {
                extras = (Map<String, Object>) this.android.get("extras");
            } else {
                extras = new HashMap<>();
            }
            extras.put(key, value);
            this.android.put("extras", extras);
        }
        if (Objects.nonNull(this.ios)) {
            Map<String, Object> extras = null;
            if (this.ios.get("extras") != null) {
                extras = (Map<String, Object>) this.ios.get("extras");
            } else {
                extras = new HashMap<>();
            }
            extras.put(key, value);
            this.ios.put("extras", extras);
        }
    }

}
