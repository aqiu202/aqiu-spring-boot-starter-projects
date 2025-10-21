package com.github.aqiu202.util;

import com.github.aqiu202.util.scan.FileScanner;
import com.github.aqiu202.util.scan.ResourceScanner;
import com.github.aqiu202.util.scan.ScanResource;
import com.github.aqiu202.util.scan.SpringResourceScanner;
import java.util.List;
import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    public void testScanner() {
        ResourceScanner springScanner = new SpringResourceScanner();
        ResourceScanner scanner = new FileScanner();
        List<ScanResource> scanResources1 = scanner.scanWithPackageName("com.github.aqiu202.util");
        List<ScanResource> scanResources2 = springScanner.scanWithPackageName("com.github.aqiu202.util");
        System.out.println(scanResources1);
        System.out.println(scanResources2);

    }
}
