package com.github.aqiu202.util.scan;

import java.util.List;

@FunctionalInterface
public interface ResourceParser {

    String CLASS_PREFIX = ".class";

    int CLASS_PREFIX_LENGTH = CLASS_PREFIX.length();

    List<ScanResource> parse(ScanResourceInput input);

    default String resourceProtocol() {
        return "jar";
    }

    static String resolveClassName(String fileName) {
        if (!fileName.endsWith(CLASS_PREFIX)) {
            return null;
        }
        return fileName.substring(0, fileName.length() - CLASS_PREFIX_LENGTH)
                .replace('/', '.');
    }
}
