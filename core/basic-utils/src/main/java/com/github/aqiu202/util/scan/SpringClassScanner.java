package com.github.aqiu202.util.scan;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class SpringClassScanner extends AbstractClassScanner {

    private final PathMatchingResourcePatternResolver resourcePatternResolver =
            new PathMatchingResourcePatternResolver();

    private final CachingMetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    @Override
    public Set<Class<?>> scanClasses(String packageName, boolean recursive) {
        Set<String> classNames = new LinkedHashSet<>();
        try {
            String suffix = recursive ? "/**/*.class" : "/*.class";
            String patternPath = "classpath*:" + packageName.replace('.', '/') + suffix;
            Resource[] resources = this.resourcePatternResolver.getResources(patternPath);
            for (Resource resource : resources) {
                classNames.add(this.metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName());
            }
        } catch (Throwable e) {
            return Collections.emptySet();
        }
        return this.filterAndConvertToClass(classNames);
    }


}

