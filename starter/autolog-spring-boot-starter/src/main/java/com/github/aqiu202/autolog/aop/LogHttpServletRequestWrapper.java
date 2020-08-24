package com.github.aqiu202.autolog.aop;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class LogHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public LogHttpServletRequestWrapper(HttpServletRequest request)
            throws IOException {
        super(request);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream in = request.getInputStream();
        byte[] buff = new byte[1024]; //buff用于存放循环读取的临时数据
        int rc;
        while ((rc = in.read(buff, 0, 100)) != -1) {
            baos.write(buff, 0, rc);
        }
        this.body = baos.toByteArray();
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);
        return new ServletInputStream() {
            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }
        };
    }

    public String getBody() {
        return new String(this.body, StandardCharsets.UTF_8);
    }

}
