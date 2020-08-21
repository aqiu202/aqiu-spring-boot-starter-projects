package com.github.aqiu202.captcha.captcha;

import com.github.aqiu202.captcha.exp.CaptchaServletException;
import com.github.aqiu202.captcha.text.TextWrapper;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for creating captcha image with a text drawn on it.
 */
public interface CaptchaProducer {

    BufferedImage createImage(@Nonnull TextWrapper textWrapper);

    BufferedImage createImage(@Nonnull TextWrapper textWrapper, int width, int height);

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text
     *            the distorted characters
     * @return image with the text
     */
    BufferedImage createImage(String text);

    BufferedImage createImage(String text, int width, int height);

    /**
     * @return the text to be drawn
     */
    String createText();

    void writeToResponse(String text, int width, int height, HttpServletResponse response)
            throws CaptchaServletException;

    void writeToResponse(String text, HttpServletResponse response) throws CaptchaServletException;

    String writeToResponse(HttpServletResponse response) throws CaptchaServletException;

    void writeToResponse(BufferedImage image, HttpServletResponse response)
            throws CaptchaServletException;
}