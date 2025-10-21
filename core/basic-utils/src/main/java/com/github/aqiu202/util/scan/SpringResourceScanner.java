package com.github.aqiu202.util.scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class SpringResourceScanner implements ResourceScanner {

    private final PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    @Override
    public List<ScanResource> scanWithPackageName(String packageName, boolean recursive) {
        List<ScanResource> scanResources = new ArrayList<>();
        try {
            String suffix = recursive ? "/**/*.class" : "/*.class";
            String patternPath = "classpath*:" + packageName.replace('.', '/') + suffix;
            Resource[] resources = this.resourcePatternResolver.getResources(patternPath);
            for (Resource resource : resources) {
                scanResources.add(new SimpleScanResource(resource.getFilename(), resource.getInputStream()));
            }
        } catch (Throwable e) {
            return Collections.emptyList();
        }
        return scanResources;
    }

}

