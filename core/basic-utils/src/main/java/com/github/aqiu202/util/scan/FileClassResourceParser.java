package com.github.aqiu202.util.scan;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 本地文件Class资源解析器
 */
public class FileClassResourceParser implements ClassResourceParser {
    @Override
    public Collection<String> parse(ScanResourceItem input) {
        return this.scanFile(input);
    }

    @Override
    public String resourceProtocol() {
        return "file";
    }

    private Set<String> scanFile(ScanResourceItem scanResource) {
        File file = new File(scanResource.getResource().getFile());
        String packageName = scanResource.getBasePackage();
        boolean recursive = scanResource.isRecursive();
        Set<String> classNames = new LinkedHashSet<>();
        try {
            if (file.isDirectory()) {
                // 如果需要递归扫描子目录
                if (recursive) {
                    // 使用Files.walkFileTree进行递归访问
                    Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            // 只处理class文件
                            if (path.toString().endsWith(CLASS_PREFIX)) {
                                classNames.add(FileClassResourceParser.this.getClassName(path, packageName));
                            }
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } else {
                    File[] files = file.listFiles((d, n) -> n.endsWith(CLASS_PREFIX));
                    if (files != null) {
                        Set<String> classSet = Arrays.stream(files)
                                .map(f -> this.getClassName(f.toPath(), packageName))
                                .filter(Objects::nonNull)
                                .collect(Collectors.toCollection(LinkedHashSet::new));
                        classNames.addAll(classSet);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classNames;
    }

    private String getClassName(Path path, String packageName) {
        String filePath;
        try {
            filePath = path.toUri().toURL().getPath();
        } catch (MalformedURLException e) {
            return null;
        }
        String patternPath = filePath.replaceAll("[/\\\\]", ".");
        int start = patternPath.indexOf(packageName);
        return patternPath.substring(start, patternPath.length() - CLASS_PREFIX_LENGTH);
    }
}
