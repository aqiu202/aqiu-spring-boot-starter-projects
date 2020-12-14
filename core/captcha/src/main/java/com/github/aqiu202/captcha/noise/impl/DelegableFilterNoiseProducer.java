package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import javax.annotation.Nonnull;

public class DelegableFilterNoiseProducer extends AbstractFilterableNoiseProducer {

    private final BufferedImageOp filter;

    public DelegableFilterNoiseProducer(@Nonnull BufferedImageOp filter) {
        this.filter = filter;
    }

    public BufferedImageOp getFilter() {
        return this.filter;
    }

    @Override
    public BufferedImage makeNoise(BufferedImage image) {
        ColorModel dstCM = image.getColorModel();
        BufferedImage distortedImage = new BufferedImage(dstCM,
                dstCM.createCompatibleWritableRaster(image.getWidth(), image.getHeight()),
                dstCM.isAlphaPremultiplied(), null);
        Graphics2D graphics = distortedImage.createGraphics();
        graphics.drawImage(this.filter(image), 0, 0, null, null);
        return distortedImage;
    }

    public BufferedImage filter(BufferedImage image) {
        return this.getFilter().filter(image, null);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.OTHER;
    }
}
