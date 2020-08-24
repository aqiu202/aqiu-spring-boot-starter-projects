package com.github.aqiu202.qrcode.exp;

public class QrCodeException extends RuntimeException {

    public QrCodeException(String msg) {
        super(msg);
    }

    public QrCodeException(Throwable e) {
        super(e);
    }

    public QrCodeException(String msg, Throwable e) {
        super(msg, e);
    }
}
