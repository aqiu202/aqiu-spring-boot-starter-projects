package com.github.aqiu202.qrcode.service;

import com.github.aqiu202.qrcode.param.QRCodeConfiguration;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface QRCodeService {

    BufferedImage createImage(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException;

    BufferedImage createImage(String content) throws IOException, WriterException;

    byte[] toByteArray(String content) throws IOException, WriterException;

    String toBase64Str(String content) throws Exception;

    byte[] toByteArray(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException;

    String toBase64Str(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException;

}
