package com.github.aqiu202.util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.springframework.core.io.ClassPathResource;

public final class ImageUtils {

    private ImageUtils() {
    }

    private static final String CLASS_PATH_FLAG = "classpath:";
    private static final String PREFIX_HTTP = "http://";
    private static final String PREFIX_HTTPS = "https://";

    public static boolean isOuterUrl(String url) {
        return url.startsWith(PREFIX_HTTP) || url.startsWith(PREFIX_HTTPS);
    }

    private static InputStream encodeToStream(String path) throws IOException {
        InputStream in;
        if (path.startsWith(CLASS_PATH_FLAG)) {
            ClassPathResource classPathResource = new ClassPathResource(
                    path.replace(CLASS_PATH_FLAG, ""));
            in = classPathResource.getInputStream();
        } else if (isOuterUrl(path)) {
            URL url = new URL(path);
            in = url.openStream();
        } else {
            in = new FileInputStream(path);
        }
        return in;
    }

    public static byte[] encode(String path) throws IOException {
        InputStream in = encodeToStream(path);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

}
