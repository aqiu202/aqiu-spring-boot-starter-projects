package com.github.aqiu202.qrcode.param;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Image;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConditionalOnMissingBean
@ConfigurationProperties(prefix = "qr-code.conf")
public final class QrCodeProperties {

    private int size = 300;
    private int logoSize = 60;
    private boolean needCompress = true;
    private String logo;
    private String format = "JPEG";
    private Charset charset = StandardCharsets.UTF_8;
    private ErrorCorrectionLevel correctionLevel = ErrorCorrectionLevel.H;
    private int margin = 1;
    private int rgbDark = Colors.BLACK;
    private int rgbBright = Colors.WHITE;
    private int algorithm = Image.SCALE_SMOOTH;

    public int getSize() {
        return size;
    }

    public QrCodeProperties setSize(int size) {
        this.size = size;
        return this;
    }

    public int getLogoSize() {
        return logoSize;
    }

    public QrCodeProperties setLogoSize(int logoSize) {
        this.logoSize = logoSize;
        return this;
    }

    public boolean isNeedCompress() {
        return needCompress;
    }

    public QrCodeProperties setNeedCompress(boolean needCompress) {
        this.needCompress = needCompress;
        return this;
    }

    public String getLogo() {
        return logo;
    }

    public QrCodeProperties setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public QrCodeProperties setFormat(String format) {
        this.format = format;
        return this;
    }

    public Charset getCharset() {
        return charset;
    }

    public QrCodeProperties setCharset(Charset charset) {
        this.charset = charset;
        return this;
    }

    public ErrorCorrectionLevel getCorrectionLevel() {
        return correctionLevel;
    }

    public QrCodeProperties setCorrectionLevel(
            ErrorCorrectionLevel correctionLevel) {
        this.correctionLevel = correctionLevel;
        return this;
    }

    public int getMargin() {
        return margin;
    }

    public QrCodeProperties setMargin(int margin) {
        this.margin = margin;
        return this;
    }

    public int getRgbDark() {
        return rgbDark;
    }

    public QrCodeProperties setRgbDark(int rgbDark) {
        this.rgbDark = rgbDark;
        return this;
    }

    public int getRgbBright() {
        return rgbBright;
    }

    public QrCodeProperties setRgbBright(int rgbBright) {
        this.rgbBright = rgbBright;
        return this;
    }

    public int getAlgorithm() {
        return algorithm;
    }

    public QrCodeProperties setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
        return this;
    }
}
