package com.github.aqiu202.util.scan;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
/**
 * jar资源解析器
 */
public class JarClassResourceParser implements ClassResourceParser {
    @Override
    public Collection<String> parse(ScanResourceItem input) {
        try {
            return this.scanJar(input);
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private Set<String> scanJar(ScanResourceItem scanResource) throws IOException {
        JarFile jarFile = ((JarURLConnection) scanResource.getResource().openConnection()).getJarFile();
        String packageName = scanResource.getBasePackage();
        boolean recursive = scanResource.isRecursive();
        // 只遍历class类型的资源
        Predicate<String> classFilter = n -> n.endsWith(CLASS_PREFIX);
        // 是否扫描子包的扫描逻辑
        Predicate<String> packageNameFilter = recursive ? n -> n.startsWith(packageName) : n -> n.substring(0, n.lastIndexOf(".")).equals(packageName);
        // 将资源路径转换为className的逻辑
        Function<String, String> packageNameResolver = n -> n.substring(0, n.length() - CLASS_PREFIX_LENGTH).replace("/", ".");
        return jarFile.stream().map(JarEntry::getName)
                .filter(classFilter)
                .map(packageNameResolver)
                .filter(packageNameFilter)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
