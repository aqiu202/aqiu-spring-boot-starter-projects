package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.WaterProperties;
import com.jhlabs.image.WaterFilter;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class WaterNoise extends AbstractFilterableNoiseProducer {

    private final WaterFilter filter;

    public WaterNoise() {
        this.filter = new WaterFilter();
    }

    public WaterNoise(@Nonnull WaterProperties properties) {
        this();
        this.filter.setCentreX(properties.getCentreX());
        this.filter.setCentreY(properties.getCentreY());
        this.filter.setWavelength(properties.getWavelength());
        this.filter.setAmplitude(properties.getAmplitude());
        this.filter.setPhase(properties.getPhase());
        this.filter.setRadius(properties.getRadius());
        this.filter.setInterpolation(properties.getInterpolation());
        this.filter.setEdgeAction(properties.getEdgeAction());
    }

    public WaterNoise(@Nonnull WaterFilter filter) {
        this.filter = filter;
    }

    public WaterNoise setWavelength(float wavelength) {
        this.filter.setWavelength(wavelength);
        return this;
    }

    public WaterNoise setAmplitude(float amplitude) {
        this.filter.setAmplitude(amplitude);
        return this;
    }

    public WaterNoise setPhase(float phase) {
        this.filter.setPhase(phase);
        return this;
    }

    public WaterNoise setCentreX(float centreX) {
        this.filter.setCentreX(centreX);
        return this;
    }

    public WaterNoise setCentreY(float centreY) {
        this.filter.setCentreY(centreY);
        return this;
    }

    public WaterNoise setRadius(float radius) {
        this.filter.setRadius(radius);
        return this;
    }

    public WaterNoise setInterpolation(int interpolation) {
        this.filter.setInterpolation(interpolation);
        return this;
    }

    public WaterNoise setEdgeAction(int edgeAction) {
        this.filter.setEdgeAction(edgeAction);
        return this;
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        return this.filter.filter(image, null);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.WATER;
    }
}
