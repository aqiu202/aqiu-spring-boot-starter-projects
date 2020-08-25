package com.github.aqiu202.qrcode.service;

import com.github.aqiu202.qrcode.exp.QrCodeException;
import com.github.aqiu202.qrcode.exp.QrCodeServletException;
import com.github.aqiu202.qrcode.param.QrCodeProperties;
import java.awt.image.BufferedImage;
import javax.servlet.http.HttpServletResponse;

public interface QrCodeService {

    BufferedImage createImage(String content, QrCodeProperties configuration)
            throws QrCodeException;

    BufferedImage createImage(String content) throws QrCodeException;

    byte[] toByteArray(String content) throws QrCodeException;

    String toBase64Str(String content) throws QrCodeException;

    byte[] toByteArray(String content, QrCodeProperties configuration)
            throws QrCodeException;

    String toBase64Str(String content, QrCodeProperties configuration)
            throws QrCodeException;

    void writeToResponse(HttpServletResponse response, String content, QrCodeProperties configuration)
            throws QrCodeException, QrCodeServletException;

    void writeToResponse(HttpServletResponse response, String content)
            throws QrCodeException, QrCodeServletException;

}
