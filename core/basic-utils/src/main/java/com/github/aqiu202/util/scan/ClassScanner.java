package com.github.aqiu202.util.scan;

import com.github.aqiu202.util.scan.filter.ClassFilter;
import com.github.aqiu202.util.scan.filter.ClassNameFilter;
import com.github.aqiu202.util.scan.filter.Filters;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Class扫描器的抽象接口
 */
public interface ClassScanner {

    void setClassLoader(ClassLoader cl);

    default Set<Class<?>> scanClasses(String packageName) {
        return this.scanClasses(packageName, false);
    }

    default Set<Class<?>> scanClasses(Collection<String> packageNames) {
        Set<Class<?>> result = new LinkedHashSet<>();
        for (String packageName : packageNames) {
            result.addAll(this.scanClasses(packageName));
        }
        return result;
    }

    Set<Class<?>> scanClasses(String packageName, boolean recursive);

    default Set<Class<?>> scanClasses(Collection<String> packageNames, boolean recursive) {
        Set<Class<?>> result = new LinkedHashSet<>();
        for (String packageName : packageNames) {
            result.addAll(this.scanClasses(packageName, recursive));
        }
        return result;
    }

    void setClassFilters(Filters<Class<?>> classFilters);

    void setNameFilters(Filters<String> nameFilters);

    Filters<String> getNameFilters();

    Filters<Class<?>> getClassFilters();

    /**
     * 添加 className的过滤器
     *
     * @param nameFilter className过滤器
     */
    void addNameFilter(ClassNameFilter nameFilter);

    /**
     * 添加 class的过滤器
     *
     * @param classFilter class过滤器
     */
    void addClassFilter(ClassFilter classFilter);
}
