package com.github.aqiu202.util.scan;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileResourceParser implements ResourceParser {
    @Override
    public List<ScanResource> parse(ScanResourceInput input) {
        return this.scanFile(input);
    }

    @Override
    public String resourceProtocol() {
        return "file";
    }

    private List<ScanResource> scanFile(ScanResourceInput scanResource) {
        File file = new File(scanResource.getResource().getFile());
        String packageName = scanResource.getBasePackage();
        boolean recursive = scanResource.isRecursive();
        List<ScanResource> resources = new ArrayList<>();
        try {
            if (file.isDirectory()) {
                if (recursive) {
                    Files.walkFileTree(file.toPath(), new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
                            String filePath = path.toUri().toURL().getPath();
                            String className = FileResourceParser.this.resolveFilePath(filePath, packageName);
                            resources.add(new FileResource(path.toFile(), className));
                            return FileVisitResult.CONTINUE;
                        }
                    });
                } else {
                    File[] files = file.listFiles();
                    if (files != null) {
                        List<ScanResource> resourseList = Arrays.stream(files).sorted(Comparator.reverseOrder()).map(f -> {
                                    String filePath;
                                    try {
                                        filePath = f.toPath().toUri().toURL().getPath();
                                    } catch (MalformedURLException e) {
                                        return null;
                                    }
                                    return new FileResource(f, this.resolveFilePath(filePath, packageName));
                                }).filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        resources.addAll(resourseList);
                    }
                }
            } else {
                resources.add(new FileResource(file, this.resolveFilePath(file.getPath(), packageName)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resources;
    }

    protected String resolveFilePath(String filePath, String packageName) {
        String path = filePath.replace("\\", "/");
        int start = path.indexOf(packageName);
        return path.substring(start);
    }
}
