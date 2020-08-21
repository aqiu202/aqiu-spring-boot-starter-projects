package com.github.aqiu202.captcha.noise;

import java.awt.image.BufferedImage;

/**
 * {@link SimpleNoiseProducer} is responsible for adding noise to an image.
 */
public interface SimpleNoiseProducer extends NoiseProducer, NoiseStyleWrapper {

    /**
     * Adds noise to an image.
     *
     * @param image the image to add the noise to
     * @param count the count for noise
     * @return the image with noise
     */
    BufferedImage makeNoise(BufferedImage image, int count);
}