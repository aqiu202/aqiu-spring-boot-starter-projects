package com.github.aqiu202.util.scan;

import java.net.URL;

public interface ScanResourceInput {
    URL getResource();

    String getBasePackage();

    default boolean isRecursive() {
        return true;
    }
}
