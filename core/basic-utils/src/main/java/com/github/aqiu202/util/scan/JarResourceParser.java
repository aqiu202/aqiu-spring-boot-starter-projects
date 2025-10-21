package com.github.aqiu202.util.scan;

import java.io.IOException;
import java.net.JarURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class JarResourceParser implements ResourceParser {
    @Override
    public List<ScanResource> parse(ScanResourceInput input) {
        try {
            return this.scanJar(input);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    private List<ScanResource> scanJar(ScanResourceInput scanResource) throws IOException {
        JarFile jarFile = ((JarURLConnection) scanResource.getResource().openConnection()).getJarFile();
        String packageName = scanResource.getBasePackage();
        boolean recursive = scanResource.isRecursive();
        Predicate<ScanResource> resourceFilter = resource -> {
            if (resource == null) {
                return false;
            }
            String fileName = resource.getFileName();
            if (fileName.equals(packageName)) {
                return true;
            }
            int index = fileName.lastIndexOf("/");
            if (index == -1) {
                return false;
            }
            return recursive ? fileName.startsWith(packageName) : fileName.substring(0, index).equals(packageName);
        };
        Function<JarEntry, ScanResource> jarEntityResolver = entity -> {
            if (entity.isDirectory()) {
                return null;
            }
            String name = entity.getName();
            return new JarResource(jarFile, entity, name);
        };
        return jarFile.stream()
                .map(jarEntityResolver)
                .filter(resourceFilter)
                .collect(Collectors.toList());
    }
}
