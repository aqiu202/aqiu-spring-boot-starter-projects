package com.github.aqiu202.qrcode.util;

public class UrlUtils {

    private UrlUtils() {
    }

    private static final String PREFIX_HTTP = "http://";
    private static final String PREFIX_HTTPS = "https://";

    public static boolean isOuterUrl(String url) {
        return url.startsWith(PREFIX_HTTP) || url.startsWith(PREFIX_HTTPS);
    }

}
