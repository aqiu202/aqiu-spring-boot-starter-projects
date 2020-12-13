package com.github.aqiu202.captcha.noise.impl;

import com.github.aqiu202.captcha.noise.SimpleNoiseProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.LineProperties;
import com.github.aqiu202.util.ColorUtils;
import com.github.aqiu202.util.RandomUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

/**
 * The default implementation of {@link SimpleNoiseProducer}, adds a noise on an
 * image.
 */
public class LineNoise implements SimpleNoiseProducer {

    private Color color;

    private int rgbStart = ColorUtils.COLOR_RGB_MIDDLE_START;
    private int rgbEnd = ColorUtils.COLOR_RGB_MIDDLE_END;

    private int count = 1;

    public LineNoise(@Nonnull LineProperties properties) {
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
        this.count = properties.getCount();
    }

    public LineNoise(int rgbStart, int rgbEnd) {
        this.rgbStart = rgbStart;
        this.rgbEnd = rgbEnd;
    }

    public LineNoise() {
    }

    public LineNoise(Color color) {
        this.color = color;
    }

    public int getRgbStart() {
        return rgbStart;
    }

    public LineNoise setRgbStart(int rgbStart) {
        this.rgbStart = rgbStart;
        return this;
    }

    public int getRgbEnd() {
        return rgbEnd;
    }

    public LineNoise setRgbEnd(int rgbEnd) {
        this.rgbEnd = rgbEnd;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public LineNoise setColor(Color color) {
        this.color = color;
        return this;
    }

    public int getCount() {
        return count;
    }

    public LineNoise setCount(int count) {
        this.count = count;
        return this;
    }

    /**
     * Draws a noise on the image. The noise curve depends on the factor values.
     * Noise won't be visible if all factors have the value more than 1.0f
     *
     * @param image the image to add the noise to
     */
    public BufferedImage makeNoise(BufferedImage image, int count) {

        // image size
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graph = image.createGraphics();

        // the points where the line changes the stroke and direction
        Point2D[] pts;
        boolean singleColor = this.color != null;
        for (int m = 0; m < count; m++) {
            Color color = singleColor ? this.color : this.getRandColor();

            // the curve from where the points are taken
            CubicCurve2D cc = new CubicCurve2D.Float(width * RandomUtils.nextFloat(), height
                    * RandomUtils.nextFloat(), width * RandomUtils.nextFloat(), height
                    * RandomUtils.nextFloat(), width * RandomUtils.nextFloat(), height
                    * RandomUtils.nextFloat(), width * RandomUtils.nextFloat(), height
                    * RandomUtils.nextFloat());

            // creates an iterator to define the boundary of the flattened curve
            PathIterator pi = cc.getPathIterator(null, 2);
            Point2D[] tmp = new Point2D[200];
            int i = 0;

            // while pi is iterating the curve, adds points to tmp array
            while (!pi.isDone()) {
                float[] coords = new float[6];
                switch (pi.currentSegment(coords)) {
                    case PathIterator.SEG_MOVETO:
                    case PathIterator.SEG_LINETO:
                        tmp[i] = new Point2D.Float(coords[0], coords[1]);
                }
                i++;
                pi.next();
            }

            pts = new Point2D[i];
            System.arraycopy(tmp, 0, pts, 0, i);

            graph.setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
            graph.setColor(color);

            // for the maximum 3 point change the stroke and direction
            for (i = 0; i < pts.length - 1; i++) {
                if (i < 3) {
                    graph.setStroke(new BasicStroke(0.9f * (4 - i)));
                }
                graph.drawLine((int) pts[i].getX(), (int) pts[i].getY(),
                        (int) pts[i + 1].getX(), (int) pts[i + 1].getY());
            }
        }

        graph.dispose();
        return image;
    }

    private Color getRandColor() {
        return ColorUtils.randomColor(rgbStart, rgbEnd);
    }

    @Override
    public BufferedImage makeNoise(BufferedImage image) {
        return this.makeNoise(image, this.count);
    }

    @Override
    public NoiseStyle getNoiseStyle() {
        return NoiseStyle.LINE;
    }
}
