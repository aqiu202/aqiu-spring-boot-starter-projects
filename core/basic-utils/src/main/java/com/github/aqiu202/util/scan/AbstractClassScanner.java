package com.github.aqiu202.util.scan;

import com.github.aqiu202.util.scan.filter.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractClassScanner implements ClassScanner {

    private static final Logger log = LoggerFactory.getLogger(AbstractClassScanner.class);

    protected ClassLoader cl;
    protected ScanFilters<String> nameFilters = new AndNameFilters();
    protected ScanFilters<Class<?>> classFilters = new AndClassFilters();

    protected AbstractClassScanner() {
        this.loadClassLoader();
    }

    @Override
    public void setClassFilters(ScanFilters<Class<?>> classFilters) {
        if (classFilters != null) {
            this.classFilters = classFilters;
        }
    }

    @Override
    public void setNameFilters(ScanFilters<String> nameFilters) {
        if (nameFilters != null) {
            this.nameFilters = nameFilters;
        }
    }

    /**
     * 添加 className的过滤器
     *
     * @param nameFilter className过滤器
     */
    @Override
    public void addNameFilter(ClassNameFilter nameFilter) {
        if (nameFilter != null) {
            this.nameFilters.addFilter(nameFilter);
        }
    }

    /**
     * 添加 class的过滤器
     *
     * @param classFilter class过滤器
     */
    @Override
    public void addClassFilter(ClassFilter classFilter) {
        if (classFilter != null) {
            this.classFilters.addFilter(classFilter);
        }
    }

    @Override
    public ScanFilters<String> getNameFilters() {
        return nameFilters;
    }

    @Override
    public ScanFilters<Class<?>> getClassFilters() {
        return classFilters;
    }

    @Override
    public void setClassLoader(ClassLoader cl) {
        this.cl = cl;
    }

    /**
     * 加载ClassLoader
     */
    protected void loadClassLoader() {
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

    /**
     * 执行扫描 （不扫描子包）
     *
     * @param packageName 包名
     * @return class集合
     */
    @Override
    public Set<Class<?>> scanClasses(String packageName) {
        return this.scanClasses(packageName, false);
    }

    /**
     * 扫描到的className集合转换为class集合（包含过滤逻辑）
     *
     * @param classes className集合
     * @return class集合
     */
    protected Set<Class<?>> filterAndConvertToClass(Collection<String> classes) {
        return classes.stream()
                .filter(this::filterClassName)
                .map(this::resolveClass)
                .filter(Objects::nonNull)
                .filter(this::filterClass)
                .collect(Collectors.toCollection(LinkedHashSet::new));

    }

    protected boolean filterClassName(String className) {
        return this.nameFilters.matches(className);
    }

    protected boolean filterClass(Class<?> clazz) {
        return this.classFilters.matches(clazz);
    }

    /**
     * className转为class
     *
     * @param className className
     * @return class对象
     */
    protected Class<?> resolveClass(String className) {
        try {
            return this.cl.loadClass(className);
        } catch (Throwable e) {
            log.warn(String.format("尝试加载Class-%s 异常", className), e);
            return null;
        }
    }
}
