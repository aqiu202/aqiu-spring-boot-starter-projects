package com.github.aqiu202.qrcode.exp;

public class QrCodeServletException extends RuntimeException {

    public QrCodeServletException(String msg) {
        super(msg);
    }

    public QrCodeServletException(Throwable e) {
        super(e);
    }

    public QrCodeServletException(String msg, Throwable e) {
        super(msg, e);
    }
}
