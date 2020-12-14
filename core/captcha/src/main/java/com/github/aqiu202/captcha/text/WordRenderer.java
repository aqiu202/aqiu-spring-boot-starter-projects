package com.github.aqiu202.captcha.text;

import java.awt.image.BufferedImage;

/**
 * {@link WordRenderer} is responsible for rendering words.
 */
public interface WordRenderer {

    int DEFAULT_WIDTH = 200;

    int DEFAULT_HEIGHT = 80;

    BufferedImage renderWord(String word);

    BufferedImage renderWord(String word, int width, int height);
}
