package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.SimpleNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.PointProperties;
import com.github.aqiu202.util.color.ColorUtils;
import com.github.aqiu202.util.RandomUtils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class PointNoise implements SimpleNoiseProducer {

    private Color color;
    private int rgbStart = ColorUtils.COLOR_RGB_START;
    private int rgbEnd = ColorUtils.COLOR_RGB_END;

    private float pointRate = 0.025f;

    public float getPointRate() {
        return pointRate;
    }

    public PointNoise setPointRate(float pointRate) {
        this.pointRate = pointRate;
        return this;
    }

    public PointNoise(@Nonnull PointProperties properties) {
        Integer color = properties.getColor();
        if (color != null) {
            this.color = new Color(color);
        }
        Float pointRate = properties.getPointRate();
        if (pointRate != null) {
            this.pointRate = pointRate;
        }
        Integer start = properties.getRgbStart();
        if (start != null) {
            this.rgbStart = start;
        }
        Integer end = properties.getRgbEnd();
        if (end != null) {
            this.rgbEnd = end;
        }
    }

    public PointNoise(Color color) {
        this.color = color;
    }

    public PointNoise(int rgbStart, int rgbEnd) {
        this.color = null;
        this.rgbStart = rgbStart;
        this.rgbEnd = rgbEnd;
    }

    public PointNoise() {
    }

    public Color getColor() {
        return color;
    }

    public PointNoise setColor(Color color) {
        this.color = color;
        return this;
    }

    public int getRgbStart() {
        return rgbStart;
    }

    public PointNoise setRgbStart(int rgbStart) {
        this.rgbStart = rgbStart;
        return this;
    }

    public int getRgbEnd() {
        return rgbEnd;
    }

    public PointNoise setRgbEnd(int rgbEnd) {
        this.rgbEnd = rgbEnd;
        return this;
    }

    @Override
    public BufferedImage makeNoise(BufferedImage image, int count) {
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D g2 = image.createGraphics();
        // 添加噪点
        boolean singleColor = this.color != null;
        for (int i = 0; i < count; i++) {
            int x = RandomUtils.nextInt(width);
            int y = RandomUtils.nextInt(height);
            int rgb = singleColor ? this.color.getRGB()
                    : ColorUtils.getRandomIntColor(this.rgbStart, this.rgbEnd);
            image.setRGB(x, y, rgb);
        }
        g2.dispose();
        return image;
    }

    @Override
    public BufferedImage makeNoise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int count = (int) (this.getPointRate() * width * height);
        return this.makeNoise(image, count);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.POINT;
    }
}
