package com.github.aqiu202.captcha.noise;

import java.awt.image.BufferedImage;

/**
 * {@link OrderedNoiseProducer} is responsible for adding noise to an image.
 */
public interface OrderedNoiseProducer {

    /**
     * Adds noise to an image before adding background to the image.
     *
     * @param image the image to add the noise to
     * @return the image with noise
     */
    BufferedImage makeNoiseBeforeAddBackground(BufferedImage image);

    /**
     * Adds noise to an image after adding background to the image.
     *
     * @param image the image to add the noise to
     * @return the image with noise
     */
    BufferedImage makeNoiseAfterAddBackground(BufferedImage image);

}