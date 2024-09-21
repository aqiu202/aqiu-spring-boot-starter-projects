package com.github.aqiu202.util;

import java.io.*;

public abstract class IOUtils {

    /**
     * 将InputStream中的所有数据读取到byte[]数组中。
     *
     * @param inputStream 输入流
     * @return 包含输入流中所有数据的byte[]数组
     * @throws IOException 如果读取过程中发生错误
     */
    public static byte[] read(InputStream inputStream) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    }

    /**
     * 将byte[]数组写入到OutputStream中。
     *
     * @param outputStream 输出流
     * @param data         要写入的数据
     * @throws IOException 如果写入过程中发生错误
     */
    public static void write(OutputStream outputStream, byte[] data) throws IOException {
        copy(new ByteArrayInputStream(data), outputStream);
    }

    /**
     * 复制InputStream到OutputStream
     * @param inputStream 输入流
     * @param outputStream 输出流
     * @throws IOException 如果复制过程中发生错误
     */
    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
    }
}
