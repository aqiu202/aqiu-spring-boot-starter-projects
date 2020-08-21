package com.github.aqiu202.captcha.text.impl;

import com.github.aqiu202.captcha.props.CaptchaProperties.RenderProperties;
import com.github.aqiu202.captcha.text.WordRenderer;
import com.github.aqiu202.captcha.util.ColorUtils;
import com.github.aqiu202.captcha.util.RandomUtils;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The default implementation of {@link WordRenderer}, creates an image with a
 * word rendered on it.
 */
public class DefaultWordRenderer implements WordRenderer {

    private float marginPercentsWidth = 0.15f;

    private float fontPercentsSize = 0.65f;

    private Color color = null;

    private int rgbStart = ColorUtils.COLOR_RGB_START;
    private int rgbEnd = ColorUtils.COLOR_RGB_DEEP_END;

    public DefaultWordRenderer() {
    }

    public DefaultWordRenderer(RenderProperties properties) {
        Float margin = properties.getMarginPercentsWidth();
        if (margin != null) {
            this.marginPercentsWidth = margin;
        }
        Float fontSize = properties.getFontPercentsSize();
        if (fontSize != null) {
            this.fontPercentsSize = fontSize;
        }
        List<String> fonts = properties.getFontFamilies();
        if (fonts != null && !fonts.isEmpty()) {
            this.fontNames = fonts;
        }
        Integer color = properties.getColor();
        if (color != null) {
            this.color = new Color(color);
        }
        Integer start = properties.getRgbStart();
        if (start != null) {
            this.rgbStart = start;
        }
        Integer end = properties.getRgbEnd();
        if (end != null) {
            this.rgbEnd = end;
        }
    }

    public void setRgbStart(int rgbStart) {
        this.rgbStart = rgbStart;
    }

    public void setRgbEnd(int rgbEnd) {
        this.rgbEnd = rgbEnd;
    }

    private List<String> fontNames = Arrays.asList("Arial", "Courier");

    public float getMarginPercentsWidth() {
        return marginPercentsWidth;
    }

    public void setMarginPercentsWidth(float marginPercentsWidth) {
        this.marginPercentsWidth = marginPercentsWidth;
    }

    public float getFontPercentsSize() {
        return fontPercentsSize;
    }

    public void setFontPercentsSize(float fontPercentsSize) {
        this.fontPercentsSize = fontPercentsSize;
    }

    public List<String> getFontNames() {
        return fontNames;
    }

    public void setFontNames(List<String> fontNames) {
        this.fontNames = fontNames;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }


    /**
     * Renders a word to an image.
     *
     * @param word
     *            The word to be rendered.
     * @param width
     *            The width of the image to be created.
     * @param height
     *            The height of the image to be created.
     * @return The BufferedImage created from the word.
     */
    public BufferedImage renderWord(String word, int width, int height) {
        Color renderColor = this.color == null ? ColorUtils.randomColor(this.rgbStart, this.rgbEnd)
                : this.color;
        int padding = (int) (width * this.getMarginPercentsWidth());
        int fontSize = (int) (height * this.getFontPercentsSize());
        List<Font> fonts = this.fontNames.stream().map(name -> new Font(name, Font.BOLD, fontSize))
                .collect(
                        Collectors.toList());
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();
        g2D.setColor(renderColor);

        RenderingHints hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);

        FontRenderContext frc = g2D.getFontRenderContext();

        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int[] charHeights = new int[wordChars.length];
        double wordsWidth = 0;
        for (int i = 0; i < wordChars.length; i++) {
            chosenFonts[i] = fonts.get(RandomUtils.nextInt(fonts.size()));
            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            double realWidth = gv.getVisualBounds().getWidth();
            double realHeight = gv.getVisualBounds().getHeight();
            charWidths[i] = (int) realWidth;
            charHeights[i] = (int) realHeight;
            wordsWidth += realWidth;
        }
        int maxHeight = Arrays.stream(charHeights).max().orElse(0);
        int startPosY = (height - maxHeight) / 2 + maxHeight;
        //空白平均分配
        int charSpace = (width - (int) wordsWidth - padding * 2) / (wordChars.length - 1);
        int startPosX = padding;
        for (int i = 0; i < wordChars.length; i++) {
            g2D.setFont(chosenFonts[i]);
            char[] charToDraw = new char[]{
                    wordChars[i]
            };
            double theta = Math.PI * (RandomUtils.nextInt(30) / 180D);
            if (RandomUtils.nextBoolean()) {
                theta = -theta;
            }
            AffineTransform affine = AffineTransform.getRotateInstance(theta,
                    startPosX + charWidths[i] * 0.5, startPosY);
            g2D.setTransform(affine);
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charSpace + charWidths[i];
        }
        return image;
    }


    @Override
    public BufferedImage renderWord(String word) {
        return this.renderWord(word, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}
