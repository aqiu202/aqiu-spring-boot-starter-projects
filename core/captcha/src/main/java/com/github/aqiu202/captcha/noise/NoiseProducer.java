package com.github.aqiu202.captcha.noise;

import java.awt.image.BufferedImage;

/**
 * {@link NoiseProducer} is responsible for adding noise to an image.
 */
public interface NoiseProducer extends NoiseStyleWrapper {

    /**
     * Adds noise to an image.
     *
     * @param image the image to add the noise to
     * @return the image with noise
     */
    BufferedImage makeNoise(BufferedImage image);

}