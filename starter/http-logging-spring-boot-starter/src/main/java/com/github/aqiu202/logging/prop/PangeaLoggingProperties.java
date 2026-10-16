package com.github.aqiu202.logging.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "pangea.logging")
public class PangeaLoggingProperties {

    /**
     * 是否启用请求日志追踪组件
     */
    private boolean enabled = true;

    private boolean includeClientInfo = true;

    private boolean includeHeaders = true;

    private boolean includeQueryString = true;

    private boolean includePayload = true;

    private int maxPayloadSize = 6400;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isIncludeClientInfo() {
        return includeClientInfo;
    }

    public void setIncludeClientInfo(boolean includeClientInfo) {
        this.includeClientInfo = includeClientInfo;
    }

    public boolean isIncludeHeaders() {
        return includeHeaders;
    }

    public void setIncludeHeaders(boolean includeHeaders) {
        this.includeHeaders = includeHeaders;
    }

    public boolean isIncludeQueryString() {
        return includeQueryString;
    }

    public void setIncludeQueryString(boolean includeQueryString) {
        this.includeQueryString = includeQueryString;
    }

    public boolean isIncludePayload() {
        return includePayload;
    }

    public void setIncludePayload(boolean includePayload) {
        this.includePayload = includePayload;
    }

    public int getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void setMaxPayloadSize(int maxPayloadSize) {
        this.maxPayloadSize = maxPayloadSize;
    }
}
