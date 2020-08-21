package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.NoiseProducer;
import java.awt.image.BufferedImage;

/**
 * Implements of NoiseProducer that does nothing.
 */
public class NoNoise implements NoiseProducer {

    public BufferedImage makeNoise(BufferedImage image) {
        //Do nothing.
        return image;
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.NONE;
    }
}
