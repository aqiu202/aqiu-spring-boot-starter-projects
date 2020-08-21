package com.github.aqiu202.captcha.noise.abs;

import com.github.aqiu202.captcha.noise.OrderedNoiseProducer;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public abstract class AbstractOrderedFilterableNoiseProducer implements OrderedNoiseProducer {

    @Override
    public BufferedImage makeNoiseBeforeAddBackground(BufferedImage image) {
        BufferedImage distortedImage = this.copyImage(image);
        Graphics2D graphics = distortedImage.createGraphics();
        graphics.drawImage(this.filterBeforeAddBackground(image), 0, 0, null, null);
        graphics.dispose();
        return distortedImage;
    }

    @Override
    public BufferedImage makeNoiseAfterAddBackground(BufferedImage image) {
        BufferedImage distortedImage = this.copyImage(image);
        Graphics2D graphics = distortedImage.createGraphics();
        graphics.drawImage(this.filterAfterAddBackground(image), 0, 0, null, null);
        graphics.dispose();
        return distortedImage;
    }

    private BufferedImage copyImage(BufferedImage image) {
        ColorModel dstCM = image.getColorModel();
        return new BufferedImage(dstCM,
                dstCM.createCompatibleWritableRaster(image.getWidth(), image.getHeight()),
                dstCM.isAlphaPremultiplied(), null);
    }

    public abstract BufferedImage filterBeforeAddBackground(BufferedImage image);

    public abstract BufferedImage filterAfterAddBackground(BufferedImage image);

}
