package com.github.aqiu202.aurora.jpush.param;

import java.util.List;
import java.util.Map;

public class JParam {

    private final Platform platform;

    private String cid;

    private Audience audience;

    private final Notification notification;

    private Map<String, Object> options;

    private JParam(Platform platform) {
        this.platform = platform;
        this.audience = new Audience();
        this.notification = Notification.of(platform);
    }

    private JParam() {
        this(Platform.All);
    }

    public static JParam newInstance() {
        return new JParam();
    }

    public static JParam of(Platform platform) {
        return new JParam(platform);
    }

    public JParam addAlias(String... alias) {
        this.audience.addAlias(alias);
        return this;
    }

    public JParam addTag(String... tag) {
        this.audience.addTag(tag);
        return this;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setAlias(List<String> alias) {
        this.audience.setAlias(alias);
    }

    public void setTags(List<String> tags) {
        this.audience.setTag(tags);
    }

    public JParam audience(Audience audience) {
        this.audience = audience;
        return this;
    }

    public JParam setTitle(String title) {
        this.notification.setTitle(title);
        return this;
    }

    public JParam setContent(String content) {
        this.notification.setContent(content);
        return this;
    }

    public JParam addExtras(Map<String, Object> value) {
        this.notification.addExtras(value);
        return this;
    }

    public JParam addExtra(String key, Object value) {
        this.notification.addExtra(key, value);
        return this;
    }

    public JParam setStyle(Style style) {
        this.notification.setStyle(style);
        return this;
    }

    public String getCid() {
        return cid;
    }

    public JParam setCid(String cid) {
        this.cid = cid;
        return this;
    }

    public Audience getAudience() {
        return audience;
    }

    public Notification getNotification() {
        return notification;
    }

    public Map<String, Object> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Object> options) {
        this.options = options;
    }

}
