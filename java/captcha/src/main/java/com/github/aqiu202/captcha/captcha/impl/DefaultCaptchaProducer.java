package com.github.aqiu202.captcha.captcha.impl;

import com.github.aqiu202.captcha.background.BackgroundProducer;
import com.github.aqiu202.captcha.background.impl.DefaultBackgroundProducer;
import com.github.aqiu202.captcha.border.BorderProducer;
import com.github.aqiu202.captcha.border.impl.DefaultBorderProducer;
import com.github.aqiu202.captcha.captcha.CaptchaProducer;
import com.github.aqiu202.captcha.exp.CaptchaServletException;
import com.github.aqiu202.captcha.noise.NoiseProducer;
import com.github.aqiu202.captcha.noise.OrderedNoiseProducer;
import com.github.aqiu202.captcha.noise.impl.CombiningNoiseProducer;
import com.github.aqiu202.captcha.noise.impl.LineNoise;
import com.github.aqiu202.captcha.noise.impl.ShadowNoise;
import com.github.aqiu202.captcha.noise.impl.ShearNoise;
import com.github.aqiu202.captcha.props.CaptchaProperties;
import com.github.aqiu202.captcha.props.CaptchaProperties.TextProperties;
import com.github.aqiu202.captcha.text.TextProducer;
import com.github.aqiu202.captcha.text.TextWrapper;
import com.github.aqiu202.captcha.text.WordRenderer;
import com.github.aqiu202.captcha.text.impl.DefaultTextCreator;
import com.github.aqiu202.captcha.text.impl.DefaultWordRenderer;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Default {@link CaptchaProducer} implementation which draws a captcha image using
 * {@link WordRenderer}, {@link NoiseProducer}, {@link BackgroundProducer}.
 * Text creation uses {@link TextProducer}.
 */
public class DefaultCaptchaProducer implements CaptchaProducer {

    private int width = WordRenderer.DEFAULT_WIDTH;
    private int height = WordRenderer.DEFAULT_HEIGHT;

    public DefaultCaptchaProducer(CaptchaProperties properties) {
        TextProperties text = properties.getText();
        this.textProducer = new DefaultTextCreator(text.getWord());
        this.wordRenderer = new DefaultWordRenderer(text.getRender());
        this.backgroundProducer = new DefaultBackgroundProducer(properties.getBackground());
        this.noiseProducer = new CombiningNoiseProducer(properties.getNoise());
        this.hasBorder = properties.isHasBorder();
        this.borderProducer = new DefaultBorderProducer(properties.getBorder());
        this.width = properties.getWidth();
        this.height = properties.getHeight();
    }

    public DefaultCaptchaProducer() {
        this.noiseProducer = new CombiningNoiseProducer(
                new LineNoise(),
                new ShearNoise(),
                new ShadowNoise());
    }

    private boolean hasBorder = true;

    private TextProducer textProducer = new DefaultTextCreator();
    private WordRenderer wordRenderer = new DefaultWordRenderer();
    private BorderProducer borderProducer = new DefaultBorderProducer();
    private BackgroundProducer backgroundProducer = new DefaultBackgroundProducer();
    private OrderedNoiseProducer noiseProducer;

    @Override
    public BufferedImage createImage(String text, int width, int height) {
        BufferedImage bi = this.wordRenderer.renderWord(text, width, height);
        bi = this.noiseProducer.makeNoiseBeforeAddBackground(bi);
        bi = this.backgroundProducer.addBackground(bi);
        bi = this.noiseProducer.makeNoiseAfterAddBackground(bi);
        if (hasBorder) {
            this.borderProducer.drawBorder(bi);
        }
        return bi;
    }

    /**
     * Create an image which will have written a distorted text.
     *
     * @param text
     *            the distorted characters
     * @return image with the text
     */
    public BufferedImage createImage(String text) {
        return this.createImage(text, this.width, this.height);
    }

    /**
     * @return the text to be drawn
     */
    public String createText() {
        return this.textProducer.getText();
    }

    @Override
    public BufferedImage createImage(@Nonnull TextWrapper textWrapper, int width, int height) {
        String text = this.createText();
        textWrapper.setValue(text);
        return this.createImage(text, width, height);
    }

    @Override
    public BufferedImage createImage(@Nonnull TextWrapper textWrapper) {
        String text = this.createText();
        textWrapper.setValue(text);
        return this.createImage(text);
    }

    @Override
    public void writeToResponse(String text, int width, int height, HttpServletResponse response) {
        BufferedImage image = this.createImage(text, width, height);
        try {
            this.writeToResponse(image, response);
        } catch (CaptchaServletException e) {
            throw new CaptchaServletException(text, e.getMessage(), e);
        }
    }

    @Override
    public void writeToResponse(String text, HttpServletResponse response) {
        this.writeToResponse(text, this.width, this.height, response);
    }

    @Override
    public String writeToResponse(HttpServletResponse response) {
        String text = this.createText();
        this.writeToResponse(text, response);
        return text;
    }

    @Override
    public void writeToResponse(BufferedImage image, HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            throw new CaptchaServletException(e);
        }
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public TextProducer getTextProducer() {
        return textProducer;
    }

    public void setTextProducer(TextProducer textProducer) {
        this.textProducer = textProducer;
    }

    public WordRenderer getWordRenderer() {
        return wordRenderer;
    }

    public void setWordRenderer(WordRenderer wordRenderer) {
        this.wordRenderer = wordRenderer;
    }

    public BorderProducer getBorderProducer() {
        return borderProducer;
    }

    public void setBorderProducer(BorderProducer borderProducer) {
        this.borderProducer = borderProducer;
    }

    public BackgroundProducer getBackgroundProducer() {
        return backgroundProducer;
    }

    public void setBackgroundProducer(BackgroundProducer backgroundProducer) {
        this.backgroundProducer = backgroundProducer;
    }

    public OrderedNoiseProducer getNoiseProducer() {
        return noiseProducer;
    }

    public void setNoiseProducer(OrderedNoiseProducer noiseProducer) {
        this.noiseProducer = noiseProducer;
    }

}
