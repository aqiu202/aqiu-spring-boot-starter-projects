package com.github.aqiu202.captcha.noise.impl;

public enum NoiseStyle {

    /**
     * 点
     */
    POINT(NoiseOrder.AFTER),
    /**
     * 线
     */
    LINE(NoiseOrder.BEFORE),
    /**
     * 波纹
     */
    RIPPLE(NoiseOrder.BEFORE),
    /**
     * 水波纹
     */
    WATER(NoiseOrder.BEFORE),
    /**
     * 阴影
     */
    SHADOW(NoiseOrder.BEFORE),
    /**
     * 扭曲
     */
    SHEAR(NoiseOrder.AFTER),
    NONE(NoiseOrder.BEFORE),
    OTHER(NoiseOrder.BEFORE);

    private final NoiseOrder order;

    NoiseStyle(NoiseOrder order) {
        this.order = order;
    }

    public NoiseOrder getOrder() {
        return this.order;
    }
}
