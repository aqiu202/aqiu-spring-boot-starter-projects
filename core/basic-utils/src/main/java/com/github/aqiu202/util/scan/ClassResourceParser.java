package com.github.aqiu202.util.scan;

import java.util.Collection;

/**
 * class资源解析器（根据文件资源获取className集合）
 */
@FunctionalInterface
public interface ClassResourceParser {

    String CLASS_PREFIX = ".class";

    int CLASS_PREFIX_LENGTH = CLASS_PREFIX.length();

    Collection<String> parse(ScanResourceItem input);

    default String resourceProtocol() {
        return "jar";
    }
}
