package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.ShadowProperties;
import com.github.aqiu202.captcha.util.RandomUtils;
import com.jhlabs.image.ShadowFilter;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class ShadowNoise extends AbstractFilterableNoiseProducer {

    private final ShadowFilter filter;

    private boolean angleRandom = true;

    public ShadowNoise() {
        this.filter = new ShadowFilter();
    }

    public ShadowNoise(@Nonnull ShadowProperties properties) {
        this();
        this.angleRandom = properties.isAngleRandom();
        this.setAngle(properties.getAngle());
        this.filter.setRadius(properties.getRadius());
        this.filter.setDistance(properties.getDistance());
        this.filter.setOpacity(properties.getOpacity());
        this.filter.setAddMargins(properties.isAddMargins());
        this.filter.setShadowOnly(properties.isShadowOnly());
        this.filter.setShadowColor(properties.getShadowColor());
    }

    public ShadowNoise(@Nonnull ShadowFilter filter) {
        this.filter = filter;
    }

    public ShadowNoise setRadius(float radius) {
        this.filter.setRadius(radius);
        return this;
    }

    public ShadowNoise setAngle(float angle) {
        this.filter.setAngle((float) Math.toRadians(angle));
        return this;
    }

    public ShadowNoise setDistance(float distance) {
        this.filter.setDistance(distance);
        return this;
    }

    public ShadowNoise setOpacity(float opacity) {
        this.filter.setOpacity(opacity);
        return this;
    }

    public ShadowNoise setAddMargins(boolean addMargins) {
        this.filter.setAddMargins(addMargins);
        return this;
    }

    public ShadowNoise setShadowOnly(boolean shadowOnly) {
        this.filter.setShadowOnly(shadowOnly);
        return this;
    }

    public ShadowNoise setShadowColor(int shadowColor) {
        this.filter.setShadowColor(shadowColor);
        return this;
    }

    public boolean isAngleRandom() {
        return angleRandom;
    }

    public ShadowNoise setAngleRandom(boolean angleRandom) {
        this.angleRandom = angleRandom;
        return this;
    }

    private void beforeFilter() {
        if (this.angleRandom) {
            this.setAngle(360 * RandomUtils.nextFloat());
        }
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        this.beforeFilter();
        return this.filter.filter(image, null);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.SHADOW;
    }
}
