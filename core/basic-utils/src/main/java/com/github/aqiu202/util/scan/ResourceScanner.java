package com.github.aqiu202.util.scan;

import java.util.List;

public interface ResourceScanner {

    default List<ScanResource> scanWithPackageName(String packageName) {
        return this.scanWithPackageName(packageName, true);
    }

    List<ScanResource> scanWithPackageName(String packageName, boolean recursive);
}
