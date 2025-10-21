package com.github.aqiu202.util.scan;

import java.io.IOException;
import java.io.InputStream;

public interface ScanResource {

    String getFileName();

    InputStream getInputStream() throws IOException;
}
