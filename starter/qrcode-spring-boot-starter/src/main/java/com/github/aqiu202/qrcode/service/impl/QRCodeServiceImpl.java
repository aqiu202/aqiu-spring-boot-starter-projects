package com.github.aqiu202.qrcode.service.impl;

import com.github.aqiu202.qrcode.param.QRCodeConfiguration;
import com.github.aqiu202.qrcode.service.QRCodeService;
import com.github.aqiu202.qrcode.util.QRCodeUtils;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QRCodeServiceImpl implements QRCodeService {

    private final QRCodeConfiguration configuration;

    public QRCodeServiceImpl(QRCodeConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public BufferedImage createImage(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException {
        return QRCodeUtils.createImage(content, configuration);
    }

    @Override
    public BufferedImage createImage(String content) throws IOException, WriterException {
        return QRCodeUtils.createImage(content, this.configuration);
    }

    @Override
    public byte[] toByteArray(String content) throws IOException, WriterException {
        return QRCodeUtils.toByteArray(content, this.configuration);
    }

    @Override
    public String toBase64Str(String content) throws IOException, WriterException {
        return QRCodeUtils.toBase64Str(content, this.configuration);
    }

    @Override
    public byte[] toByteArray(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException {
        return QRCodeUtils.toByteArray(content, configuration);
    }

    @Override
    public String toBase64Str(String content, QRCodeConfiguration configuration)
            throws IOException, WriterException {
        return QRCodeUtils.toBase64Str(content, configuration);
    }
}
