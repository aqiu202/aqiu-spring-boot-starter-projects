package com.github.aqiu202.qrcode.service.impl;

import com.github.aqiu202.qrcode.exp.QrCodeException;
import com.github.aqiu202.qrcode.exp.QrCodeServletException;
import com.github.aqiu202.qrcode.param.QrCodeProperties;
import com.github.aqiu202.qrcode.service.QrCodeService;
import com.github.aqiu202.qrcode.util.QRCodeHelper;
import com.google.zxing.WriterException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeProperties configuration;

    public QrCodeServiceImpl(QrCodeProperties configuration) {
        this.configuration = configuration;
    }

    @Override
    public BufferedImage createImage(String content, QrCodeProperties configuration) {
        try {
            return QRCodeHelper.createImage(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public BufferedImage createImage(String content) {
        try {
            return QRCodeHelper.createImage(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public byte[] toByteArray(String content) {
        try {
            return QRCodeHelper.toByteArray(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public String toBase64Str(String content) {
        try {
            return QRCodeHelper.toBase64Str(content, this.configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public byte[] toByteArray(String content, QrCodeProperties configuration) {
        try {
            return QRCodeHelper.toByteArray(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public String toBase64Str(String content, QrCodeProperties configuration) {
        try {
            return QRCodeHelper.toBase64Str(content, configuration);
        } catch (IOException | WriterException e) {
            throw new QrCodeException("生成二维码异常", e);
        }
    }

    @Override
    public void writeToResponse(HttpServletResponse response, String content,
            QrCodeProperties configuration) throws QrCodeException, QrCodeServletException {
        BufferedImage image = this.createImage(content, configuration);
        try {
            ImageIO.write(image, configuration.getFormat(), response.getOutputStream());
        } catch (IOException e) {
            throw new QrCodeServletException("二维码写入Servlet错误", e);
        }
    }

    @Override
    public void writeToResponse(HttpServletResponse response, String content)
            throws QrCodeException, QrCodeServletException {
        this.writeToResponse(response, content, this.configuration);
    }
}
