package com.github.aqiu202.util.scan;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarResource extends AbstractScanResource {

    private final JarFile jarFile;
    private final JarEntry jarEntry;

    public JarResource(JarFile jarFile, JarEntry jarEntry, String fileName) {
        super(fileName);
        this.jarFile = jarFile;
        this.jarEntry = jarEntry;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.jarFile.getInputStream(this.jarEntry);
    }
}
