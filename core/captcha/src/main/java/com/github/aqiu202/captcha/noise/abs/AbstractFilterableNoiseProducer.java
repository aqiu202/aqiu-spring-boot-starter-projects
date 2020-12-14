package com.github.aqiu202.captcha.noise.abs;

import com.github.aqiu202.captcha.noise.NoiseProducer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public abstract class AbstractFilterableNoiseProducer implements NoiseProducer {

    @Override
    public BufferedImage makeNoise(BufferedImage image) {
        ColorModel dstCM = image.getColorModel();
        BufferedImage distortedImage = new BufferedImage(dstCM,
                dstCM.createCompatibleWritableRaster(image.getWidth(), image.getHeight()),
                dstCM.isAlphaPremultiplied(), null);
        Graphics2D graphics = distortedImage.createGraphics();
        graphics.drawImage(this.filter(image), 0, 0, null, null);
        graphics.dispose();
        return distortedImage;
    }

    public abstract BufferedImage filter(BufferedImage image);

}
