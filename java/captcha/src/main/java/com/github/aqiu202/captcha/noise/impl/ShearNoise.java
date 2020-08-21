package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.abs.AbstractFilterableNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.ShearProperties;
import com.github.aqiu202.captcha.util.RandomUtils;
import com.jhlabs.image.ShearFilter;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class ShearNoise extends AbstractFilterableNoiseProducer {

    private final ShearFilter filter;

    private float xAngle = 10.0f;
    private float yAngle = 4.0f;

    /**
     * X轴角度是否正负转换
     */
    private boolean xAngleToggle = true;
    /**
     * Y轴角度是否正负转换
     */
    private boolean yAngleToggle = true;

    public ShearNoise() {
        this.filter = new ShearFilter();
    }

    public ShearNoise(@Nonnull ShearProperties properties) {
        this();
        this.xAngle = properties.getXAngle();
        this.xAngleToggle = properties.isXAngleToggle();
        this.yAngle = properties.getYAngle();
        this.yAngleToggle = properties.isYAngleToggle();
        this.filter.setResize(properties.isResize());
        this.filter.setInterpolation(properties.getInterpolation());
        this.filter.setEdgeAction(properties.getEdgeAction());
    }

    public ShearNoise(@Nonnull ShearFilter filter) {
        this.filter = filter;
    }

    public float getXAngle() {
        return xAngle;
    }

    public ShearNoise setXAngle(float xAngle) {
        this.xAngle = xAngle;
        return this;
    }

    public float getYAngle() {
        return yAngle;
    }

    public ShearNoise setYAngle(float yAngle) {
        this.yAngle = yAngle;
        return this;
    }

    public boolean isXAngleToggle() {
        return xAngleToggle;
    }

    public ShearNoise setXAngleToggle(boolean xAngleToggle) {
        this.xAngleToggle = xAngleToggle;
        return this;
    }

    public boolean isYAngleToggle() {
        return yAngleToggle;
    }

    public ShearNoise setYAngleToggle(boolean yAngleToggle) {
        this.yAngleToggle = yAngleToggle;
        return this;
    }

    public ShearNoise setInterpolation(int interpolation) {
        this.filter.setInterpolation(interpolation);
        return this;
    }

    public ShearNoise setEdgeAction(int edgeAction) {
        this.filter.setEdgeAction(edgeAction);
        return this;
    }

    private void beforeFilter() {
        float xValue = this.xAngle;
        if (this.xAngleToggle && RandomUtils.nextBoolean()) {
            xValue = -xValue;
        }
        this.filter.setXAngle((float) Math.toRadians(xValue));
        float yValue = this.yAngle;
        if (this.yAngleToggle && RandomUtils.nextBoolean()) {
            yValue = -yValue;
        }
        this.filter.setYAngle((float) Math.toRadians(yValue));
    }

    @Override
    public BufferedImage filter(BufferedImage image) {
        this.beforeFilter();
        return this.filter.filter(image, null);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.SHEAR;
    }
}
