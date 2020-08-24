package com.github.aqiu202.qrcode.service.impl;

import com.github.aqiu202.qrcode.exp.QrCodeException;
import com.github.aqiu202.qrcode.param.QrCodeProperties;
import com.github.aqiu202.qrcode.service.QrCodeService;
import com.github.aqiu202.qrcode.util.QRCodeUtils;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeProperties configuration;

    public QrCodeServiceImpl(QrCodeProperties configuration) {
        this.configuration = configuration;
    }

    @Override
    public BufferedImage createImage(String content, QrCodeProperties configuration) {
        try {
            return QRCodeUtils.createImage(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public BufferedImage createImage(String content) {
        try {
            return QRCodeUtils.createImage(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public byte[] toByteArray(String content) {
        try {
            return QRCodeUtils.toByteArray(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public String toBase64Str(String content) {
        try {
            return QRCodeUtils.toBase64Str(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public byte[] toByteArray(String content, QrCodeProperties configuration) {
        try {
            return QRCodeUtils.toByteArray(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public String toBase64Str(String content, QrCodeProperties configuration) {
        try {
            return QRCodeUtils.toBase64Str(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }
}
