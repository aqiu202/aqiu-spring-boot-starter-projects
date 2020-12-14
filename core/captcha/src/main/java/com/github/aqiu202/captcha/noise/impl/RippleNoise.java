package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.RippleProperties;
import com.jhlabs.image.RippleFilter;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class RippleNoise extends AbstractFilterableNoiseProducer {

    private final RippleFilter filter;

    public RippleNoise() {
        this.filter = new RippleFilter();
    }

    public RippleNoise(@Nonnull RippleProperties properties) {
        this();
        this.filter.setWaveType(properties.getWaveType());
        this.filter.setXAmplitude(properties.getXAmplitude());
        this.filter.setYAmplitude(properties.getYAmplitude());
        this.filter.setXWavelength(properties.getXWavelength());
        this.filter.setYWavelength(properties.getYWavelength());
        this.filter.setInterpolation(properties.getInterpolation());
        this.filter.setEdgeAction(properties.getEdgeAction());
    }

    public RippleNoise(@Nonnull RippleFilter filter) {
        this.filter = filter;
    }

    public RippleNoise setWaveType(int waveType) {
        this.filter.setWaveType(waveType);
        return this;
    }

    public RippleNoise setXAmplitude(float xAmplitude) {
        this.filter.setXAmplitude(xAmplitude);
        return this;
    }

    public RippleNoise setYAmplitude(float yAmplitude) {
        this.filter.setYAmplitude(yAmplitude);
        return this;
    }

    public RippleNoise setXWavelength(float xWavelength) {
        this.filter.setXWavelength(xWavelength);
        return this;
    }

    public RippleNoise setYWavelength(float yWavelength) {
        this.filter.setYWavelength(yWavelength);
        return this;
    }

    public RippleNoise setInterpolation(int interpolation) {
        this.filter.setInterpolation(interpolation);
        return this;
    }

    public RippleNoise setEdgeAction(int edgeAction) {
        this.filter.setEdgeAction(edgeAction);
        return this;
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        return this.filter.filter(image, null);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.RIPPLE;
    }
}
