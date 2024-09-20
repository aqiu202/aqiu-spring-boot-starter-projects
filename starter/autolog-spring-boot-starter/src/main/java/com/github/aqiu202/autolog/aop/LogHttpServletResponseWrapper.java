package com.github.aqiu202.autolog.aop;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class LogHttpServletResponseWrapper extends HttpServletResponseWrapper {

    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private ServletOutputStreamCopier copier;

    public LogHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            copier = new ServletOutputStreamCopier(outputStream);
        }

        return copier;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            copier.flush();
        }
    }

    public Map<String, List<String>> getHeaderMap() {
        Map<String, List<String>> headers = new HashMap<>();
        Collection<String> headerNames = this.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, new ArrayList<>(this.getHeaders(headerName)));
        }
        return headers;
    }

    public byte[] getResponseData() {
        if (copier != null) {
            return copier.getCopy();
        } else {
            return new byte[0];
        }
    }

    public String getResponseDataAsString() {
        return new String(this.getResponseData(), StandardCharsets.UTF_8);
    }

    /**
     * 内部类，ServletOutputStreamCopier
     */
    private static class ServletOutputStreamCopier extends ServletOutputStream {
        private final OutputStream outputStream;
        private final ByteArrayOutputStream copy;

        public ServletOutputStreamCopier(OutputStream outputStream) {
            this.outputStream = outputStream;
            this.copy = new ByteArrayOutputStream();
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            copy.write(b);
        }

        public byte[] getCopy() {
            return copy.toByteArray();
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener listener) {

        }
    }

}