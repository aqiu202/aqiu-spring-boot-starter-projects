package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.NoiseProducer;
import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import javax.annotation.Nonnull;

public class DelegableNoiseProducer extends AbstractFilterableNoiseProducer {

    private final NoiseProducer noiseProducer;

    public DelegableNoiseProducer(@Nonnull NoiseProducer noiseProducer) {
        this.noiseProducer = noiseProducer;
    }

    public NoiseProducer getNoiseProducer() {
        return this.noiseProducer;
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
        return this.getNoiseProducer().makeNoise(image);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.OTHER;
    }
}
