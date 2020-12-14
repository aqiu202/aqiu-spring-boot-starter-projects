package com.github.aqiu202.captcha.noise.impl;


/**
 * NoiseProducer执行顺序
 */
public enum NoiseOrder {
    /**
     * 在添加背景之前执行
     */
    BEFORE,
    /**
     * 在添加背景之后执行
     */
    AFTER;
}
