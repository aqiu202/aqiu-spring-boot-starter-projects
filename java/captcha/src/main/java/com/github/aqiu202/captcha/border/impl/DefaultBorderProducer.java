package com.github.aqiu202.captcha.border.impl;

import com.github.aqiu202.captcha.border.BorderProducer;
import com.github.aqiu202.captcha.props.CaptchaProperties.BorderProperties;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import javax.annotation.Nonnull;

public class DefaultBorderProducer implements BorderProducer {

    private int borderThickness = 1;
    private Color borderColor = Color.BLACK;

    public DefaultBorderProducer() {
    }

    public DefaultBorderProducer(@Nonnull BorderProperties properties) {
        Integer color = properties.getColor();
        if (color != null) {
            this.borderColor = new Color(color);
        }
        Integer thickness = properties.getThickness();
        if (thickness != null) {
            this.borderThickness = thickness;
        }
    }

    public DefaultBorderProducer(Color color, int borderThickness) {
        this.borderColor = color;
        this.borderThickness = borderThickness;
    }

    public int getBorderThickness() {
        return borderThickness;
    }

    public DefaultBorderProducer setBorderThickness(int borderThickness) {
        this.borderThickness = borderThickness;
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public DefaultBorderProducer setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        return this;
    }

    @Override
    public void drawBorder(BufferedImage image) {
        this.drawBox(image);
    }

    private void drawBox(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(this.borderColor);
        if (this.borderThickness != 1) {
            BasicStroke stroke = new BasicStroke((float) this.borderThickness);
            graphics.setStroke(stroke);
        }
        Line2D line1 = new Line2D.Double(0, 0, 0, height);
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0, 0, width, 0);
        graphics.draw(line2);
        line2 = new Line2D.Double(0, height - this.borderThickness, width,
                height - this.borderThickness);
        graphics.draw(line2);
        line2 = new Line2D.Double(width - this.borderThickness, height - this.borderThickness,
                width - this.borderThickness, 0);
        graphics.draw(line2);
    }
}
