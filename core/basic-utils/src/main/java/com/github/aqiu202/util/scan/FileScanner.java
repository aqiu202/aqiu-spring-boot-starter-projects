package com.github.aqiu202.util.scan;

import com.github.aqiu202.util.scan.filter.AndClassFilters;
import com.github.aqiu202.util.scan.filter.AndStringFilters;
import com.github.aqiu202.util.scan.filter.ScanFilters;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileScanner implements ResourceScanner {

    private static final Logger log = LoggerFactory.getLogger(FileScanner.class);

    private ClassLoader cl;

    private final Map<String, ResourceParser> classResourceParserMap = new HashMap<>();

    private ScanFilters<String> nameFilters = new AndStringFilters();
    private ScanFilters<Class<?>> classFilters = new AndClassFilters();

    public FileScanner() {
        this.loadClassLoader();
        this.registerParser(new JarResourceParser());
        this.registerParser(new FileResourceParser());
    }

    public void registerParser(ResourceParser parser) {
        this.classResourceParserMap.putIfAbsent(parser.resourceProtocol(), parser);
    }

    public void setClassFilters(@Nonnull ScanFilters<Class<?>> classFilters) {
        this.classFilters = classFilters;
    }

    public void setNameFilters(@Nonnull ScanFilters<String> nameFilters) {
        this.nameFilters = nameFilters;
    }

    public ScanFilters<String> getNameFilters() {
        return nameFilters;
    }

    public ScanFilters<Class<?>> getClassFilters() {
        return classFilters;
    }

    public void setClassLoader(@Nonnull ClassLoader cl) {
        this.cl = cl;
    }

    private void loadClassLoader() {
        if (this.cl != null) {
            return;
        }
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ignored) {
        }
        if (cl == null) {
            try {
                cl = ClassLoader.getSystemClassLoader();
            } catch (Throwable ignored) {
            }
        }
        if (cl != null) {
            this.setClassLoader(cl);
        }
    }

    public List<ScanResource> scanResources(String packageName) {
        return this.scanResources(packageName, true);
    }

    @Override
    public List<ScanResource> scanWithPackageName(String packageName, boolean recursive) {
        return this.scanResources(packageName.replaceAll("[.\\\\]","/"), recursive);
    }

    public List<ScanResource> scanResources(String resourceName, boolean recursive) {
        List<ScanResource> scanResources = new ArrayList<>();
        try {
            Enumeration<URL> resources = this.cl.getResources(resourceName);
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                ResourceParser parser = this.classResourceParserMap.get(protocol);
                if (parser != null) {
                    scanResources.addAll(parser.parse(new SimpleScanResourceInput(resource, resourceName, recursive)));
                }
            }
        } catch (Throwable e) {
            log.warn("文件扫描异常", e);
            return Collections.emptyList();
        }
        return scanResources.stream()
                .filter(resource -> this.filterFileName(resource.getFileName()))
                .collect(Collectors.toList());
    }

    public Set<Class<?>> scanClasses(String packageName) {
        return this.scanClasses(packageName, true);
    }

    public Set<Class<?>> scanClasses(String packageName, boolean recursive) {
        return this.convertToClass(this.scanWithPackageName(packageName, recursive));
    }

    private Set<Class<?>> convertToClass(Collection<ScanResource> classes) {
        return classes.stream()
                .map(r -> ResourceParser.resolveClassName(r.getFileName()))
                .filter(Objects::nonNull)
                .map(this::resolveClass)
                .filter(Objects::nonNull)
                .filter(this::filterClass)
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    private boolean filterFileName(String fileName) {
        return this.nameFilters.matches(fileName);
    }

    private boolean filterClass(Class<?> clazz) {
        return this.classFilters.matches(clazz);
    }

    private Class<?> resolveClass(String className) {
        try {
            return this.cl.loadClass(className);
        } catch (Throwable e) {
            log.warn(String.format("尝试加载Class-%s 异常", className), e);
            return null;
        }
    }

}
