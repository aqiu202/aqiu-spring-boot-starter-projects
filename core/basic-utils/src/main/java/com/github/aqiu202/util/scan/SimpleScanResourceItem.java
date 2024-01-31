package com.github.aqiu202.util.scan;

import java.net.URL;

public class SimpleScanResourceItem implements ScanResourceItem {

    private final URL resource;
    private final String basePackage;

    private boolean recursive;

    public SimpleScanResourceItem(URL resource, String basePackage) {
        this.resource = resource;
        this.basePackage = basePackage;
    }

    public SimpleScanResourceItem(URL resource, String basePackage, boolean recursive) {
        this(resource, basePackage);
        this.recursive = recursive;
    }

    @Override
    public URL getResource() {
        return this.resource;
    }

    @Override
    public String getBasePackage() {
        return this.basePackage;
    }

    @Override
    public boolean isRecursive() {
        return recursive;
    }

    public void setRecursive(boolean recursive) {
        this.recursive = recursive;
    }
}
