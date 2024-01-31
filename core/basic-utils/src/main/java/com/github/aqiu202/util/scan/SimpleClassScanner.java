package com.github.aqiu202.util.scan;

import java.net.URL;
import java.util.*;

/**
 * Class扫描器
 */
public class SimpleClassScanner extends AbstractClassScanner {

    /**
     * class资源解析器配置
     */
    private final Map<String, ClassResourceParser> classResourceParserMap = new HashMap<>();


    public SimpleClassScanner() {
        this.registerParser(new JarClassResourceParser());
        this.registerParser(new FileClassResourceParser());
    }

    /**
     * 对不同类型的资源注册不同的解析器（默认只支持jar和class文件）
     *
     * @param parser 解析器实例
     */
    public void registerParser(ClassResourceParser parser) {
        this.classResourceParserMap.putIfAbsent(parser.resourceProtocol(), parser);
    }

    /**
     * 执行扫描
     *
     * @param packageName 包名
     * @param recursive   是否递归扫描子包
     * @return class集合
     */
    @Override
    public Set<Class<?>> scanClasses(String packageName, boolean recursive) {
        Set<String> classNames = new LinkedHashSet<>();
        try {
            Enumeration<URL> resources = this.cl.getResources(packageName.replace(".", "/"));
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                String protocol = resource.getProtocol();
                // 根据资源类型查找解析器
                ClassResourceParser parser = this.classResourceParserMap.get(protocol);
                // 找到对应的解析器则进行解析，并将结果收集起来，否则跳过
                if (parser != null) {
                    classNames.addAll(parser.parse(new SimpleScanResourceItem(resource, packageName, recursive)));
                }
            }
        } catch (Throwable e) {
            return Collections.emptySet();
        }
        return this.filterAndConvertToClass(classNames);
    }


}
