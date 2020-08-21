package com.github.aqiu202.captcha.background;

import java.awt.image.BufferedImage;

/**
 * {@link BackgroundProducer} is responsible for adding background to an image.
 */
public interface BackgroundProducer {

    BufferedImage addBackground(BufferedImage image);
}
